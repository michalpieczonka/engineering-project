package pm.workout.helper.api.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pm.workout.helper.api.AbstractItTest;
import pm.workout.helper.api.authentication.request.SignInUserRequest;
import pm.workout.helper.domain.user.AppUser;
import pm.workout.helper.domain.user.UserAuthority;
import pm.workout.helper.domain.user.UserRepository;
import pm.workout.helper.infrastructure.configuration.SecurityConfiguration;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationRestResourceItTest extends AbstractItTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    SecurityConfiguration configuration;


    @Autowired
    UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
    }

    @Test
    void shouldNotLoginUseIfNotExists() throws Exception {
        //given:
        SignInUserRequest request = new SignInUserRequest("majk", "test");
        //when:
        mockMvc.perform(post("/user-service/api/authentication/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                //then:
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRegisterNewUser() throws Exception {
        //given:
        String requestString = "{\n" +
                "    \"username\":\"majk\",\n" +
                "    \"password\":\"test\",\n" +
                "    \"email\":\"michu@wp.pl\",\n" +
                "    \"dateOfBirth\": \"12.12.2022\",\n" +
                "     \"trainingStartDate\": \"12.12.2022\",\n" +
                "     \"gender\": \"MALE\"\n" +
                "}";
        //when:
        mockMvc.perform(post("/user-service/api/authentication/register")
                        .contentType(APPLICATION_JSON)
                        .content(requestString))
                //then:
                .andExpect(status().isCreated());

        //then:
        Optional<AppUser> user = userRepository.findUserByUsername("majk");
        assert user.isPresent();
        assert userRepository.getAllUsers().size() == 1;
    }

    @Test
    void shouldNotRegisterUser() throws Exception {
        //given:
        String requestString = "{\n" +
                "    \"username\":\"majk\",\n" +
                "    \"password\":\"test\",\n" +
                "    \"email\":\"michuwp.pl\",\n" +
                "    \"dateOfBirth\": \"12.12.2022\",\n" +
                "     \"trainingStartDate\": \"12.12.2022\",\n" +
                "     \"gender\": \"MALE\"\n" +
                "}";
        //when:
        mockMvc.perform(post("/user-service/api/authentication/register")
                        .contentType(APPLICATION_JSON)
                        .content(requestString))
                //then:
                .andExpect(status().isBadRequest());
    }

    @Test
    @Disabled
    void shouldReturnValidUserData() throws Exception {

        //given:
        AppUser appUser = AppUser.builder()
                .username("test")
                .password(passwordEncoder.encode("test"))
                .email("email@wp.pl")
                .userAuthority(UserAuthority.USER_AUTHORITY)
                .build();
        userRepository.saveUser(appUser);
        System.out.println( userRepository.findUserByUsername("test").get().getUsername());

        String requestString = "{\n" +
                "    \"username\":\"test\",\n" +
                "     \"password\": \"test\"\n" +
                "}";
        //when:
        mockMvc.perform(post("/user-service/api/authentication/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestString))
                //then:
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.email").value("email@wp.pl"))
                .andExpect(jsonPath("$.userAuthority").value("USER_AUTHORITY"));
    }






}
