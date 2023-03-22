package pm.workout.helper.api.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pm.workout.helper.api.AbstractItTest;
import pm.workout.helper.api.user.request.AddUserPhotoRequest;
import pm.workout.helper.domain.training.plan.PlanPriority;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.training.plan.TrainingPlanType;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanRateDto;
import pm.workout.helper.domain.user.AppUser;
import pm.workout.helper.domain.user.Gender;
import pm.workout.helper.domain.user.UserAuthority;
import pm.workout.helper.domain.user.UserRepository;
import pm.workout.helper.domain.user.health.AppUserHealthDetails;
import pm.workout.helper.domain.user.health.AppUserPhoto;
import pm.workout.helper.infrastructure.services.TrainingPlanConfigurationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class UserRestResourceItTest extends AbstractItTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingPlanConfigurationService myService;

    private static final String baseURL = "/user-service/api/users";


    @Autowired
    UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
    }

    @Test
    void shouldUpdateUserDetails() throws Exception {
        //given:
        AppUser appUser = AppUser.builder()
                .username("test")
                .email("email@wp.pl")
                .dateOfBirth(LocalDate.now())
                .registrationDateTime(LocalDateTime.now())
                .currentTrainingPlanId(1L)
                .userAuthority(UserAuthority.USER_AUTHORITY)
                .build();

        userRepository.saveUser(appUser);
        AppUser savedUser = userRepository.findUserByUsername("test").get();
        String updateRequest = "{\n" +
                "    \"username\":\"majk\",\n" +
                "    \"email\":\"michu@wp.pl\",\n" +
                "    \"gender\": \"FEMALE\",\n" +
                "     \"dateOfBirth\": \"2022-02-14\",\n" +
                "     \"trainingStartDate\": \"2022-02-14\",\n" +
                "     \"notificationsEnabled\": \"true\"\n" +
                "}";

        //when:
        mockMvc.perform(put(baseURL+"/{id}", savedUser.getId())
                        .contentType(APPLICATION_JSON)
                        .content(updateRequest))
                //then:
                .andExpect(status().isOk());

        AppUser updatedUser = userRepository.findUserByUsername("majk").get();
        assert(updatedUser.getEmail().equals("michu@wp.pl"));
        assert(updatedUser.getUsername().equals("majk"));
        assert(updatedUser.getGender() == Gender.FEMALE);
        assert(updatedUser.isNotificationsEnabled());

    }

    @Test
    void shouldReturnUserDetails() throws Exception {
        //given:
        AppUser appUser = AppUser.builder()
                .username("test")
                .email("email@wp.pl")
                .dateOfBirth(LocalDate.now())
                .registrationDateTime(LocalDateTime.now())
                .currentTrainingPlanId(1L)
                .userAuthority(UserAuthority.USER_AUTHORITY)
                .build();

        userRepository.saveUser(appUser);
        AppUser savedUser = userRepository.findUserByUsername("test").get();

        //when:
        mockMvc.perform(get(baseURL+"/{id}", savedUser.getId())
                        .contentType(APPLICATION_JSON))
                //then:
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.username").value(appUser.getUsername()))
                .andExpect(jsonPath("$.currentTrainingPlanId").value("1"));

    }

    @Test
    void shouldReturnUserTrainingPlanDetails() throws Exception {
        //given:
        AppUser appUser = AppUser.builder()
                .username("test")
                .email("email@wp.pl")
                .dateOfBirth(LocalDate.now())
                .registrationDateTime(LocalDateTime.now())
                .currentTrainingPlanId(1L)
                .userAuthority(UserAuthority.USER_AUTHORITY)
                .build();
        userRepository.saveUser(appUser);
        AppUser savedUser = userRepository.findUserByUsername("test").get();

        when(myService.getTrainingPlan(anyLong())).thenReturn(buildTrainingPlanDetailsDto(savedUser.getId()));


        //when:
        mockMvc.perform(get(baseURL+"/{id}/training-plan", savedUser.getId())
                        .contentType(APPLICATION_JSON))
                //then:
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.planCreatorUserId").value(savedUser.getId()));

    }

    @Test
    void shouldSaveUserPhoto() throws Exception {
        //given:
        AppUser appUser = AppUser.builder()
                .username("test")
                .email("email@wp.pl")
                .dateOfBirth(LocalDate.now())
                .registrationDateTime(LocalDateTime.now())
                .currentTrainingPlanId(1L)
                .userAuthority(UserAuthority.USER_AUTHORITY)
                .build();

        userRepository.saveUser(appUser);
        AppUser savedUser = userRepository.findUserByUsername("test").get();
        var request = new AddUserPhotoRequest("pdf", "plik.pdf", new byte[10]);
        //when:
        mockMvc.perform(post(baseURL+"/{id}/photos", savedUser.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                //then:
                .andExpect(status().isOk());

        AppUser updatedUser = userRepository.findUserByUsername("test").get();
        assert(updatedUser.getUserPhotos().size()==1);
        assert(updatedUser.getUserPhotos().stream().anyMatch(p -> p.getName().equals("plik.pdf")));
    }

    @Test
    void shouldReturnUserPhoto() throws Exception {
        //given:
        AppUser appUser = AppUser.builder()
                .username("test")
                .email("email@wp.pl")
                .dateOfBirth(LocalDate.now())
                .registrationDateTime(LocalDateTime.now())
                .currentTrainingPlanId(1L)
                .userAuthority(UserAuthority.USER_AUTHORITY)
                .build();

        userRepository.saveUser(appUser);
        AppUser savedUser = userRepository.findUserByUsername("test").get();

        AppUserPhoto photo = AppUserPhoto.builder().name("plik.pdf").type("pdf").imageData(new byte[10])
                .appUser(savedUser).build();
        savedUser.addPhoto(photo);

        userRepository.saveUser(savedUser);
        AppUserPhoto finalPhoto = userRepository.findUserById(savedUser.getId())
                .get().getUserPhotos()
                .stream().filter(p -> p.getName().equals("plik.pdf")).findFirst().get();

        //when:
        mockMvc.perform(get(baseURL+"/photos/{id}", finalPhoto.getId())
                        .contentType(APPLICATION_JSON))
                //then:
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.photoId").value(finalPhoto.getId()))
                .andExpect(jsonPath("$.name").value(finalPhoto.getName()));
    }

    @Test
    void shouldDeleteValidUserPhoto() throws Exception {
        //given:
        AppUser appUser = AppUser.builder()
                .username("test")
                .email("email@wp.pl")
                .dateOfBirth(LocalDate.now())
                .registrationDateTime(LocalDateTime.now())
                .currentTrainingPlanId(1L)
                .userAuthority(UserAuthority.USER_AUTHORITY)
                .build();

        userRepository.saveUser(appUser);
        AppUser savedUser = userRepository.findUserByUsername("test").get();

        AppUserPhoto photo = AppUserPhoto.builder().name("plik.pdf").type("pdf").imageData(new byte[10])
                .appUser(savedUser).build();
        savedUser.addPhoto(photo);

        userRepository.saveUser(savedUser);
        AppUserPhoto finalPhoto = userRepository.findUserById(savedUser.getId())
                .get().getUserPhotos()
                .stream().filter(p -> p.getName().equals("plik.pdf")).findFirst().get();

        //when:
        mockMvc.perform(delete(baseURL+"/photos/{id}", finalPhoto.getId())
                        .contentType(APPLICATION_JSON))
                //then:
                .andExpect(status().isNoContent());

    }


    private AppUserHealthDetails buildHealthDetails(AppUser appUser) {
        return AppUserHealthDetails.builder()
                .id(1L)
                .weight(10)
                .height(30)
                .appUser(appUser)
                .armCircumference(15)
                .waistCircuit(15)
                .waistCircumference(15)
                .thighCircumference(30)
                .build();
    }

    private AppUser buildAppUser(String userName, String email, Long userId){
        return AppUser.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .password("test")
                .dateOfBirth(LocalDate.now())
                .registrationDateTime(LocalDateTime.now())
                .gender(Gender.MALE)
                .currentTrainingPlanId(1L)
                .userAuthority(UserAuthority.USER_AUTHORITY)
                .notificationsEnabled(true)
                .build();
    }

    private TrainingPlanDetailsDto buildTrainingPlanDetailsDto(long planCreatorUserId){
        return TrainingPlanDetailsDto.builder()
                .id(1L)
                .title("test")
                .planDescription("test")
                .numberOfTrainingDays(3)
                .trainingDays(Set.of(TrainingDay.MONDAY, TrainingDay.FRIDAY))
                .planType(TrainingPlanType.PUSH_PULL)
                .planPriority(PlanPriority.CHEST)
                .preferredTrainingInternship(5)
                .planCreatorUserId(planCreatorUserId)
                .isPublic(true)
                .planRates(Set.of(new TrainingPlanRateDto(1L, "git", 5, LocalDateTime.now().minusDays(1))))
                .trainingUnits(Set.of())
                .creationDate(LocalDateTime.now().minusDays(10))
                .build();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

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
