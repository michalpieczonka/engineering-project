package pm.workout.helper.api.exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pm.workout.helper.api.AbstractItTest;
import pm.workout.helper.api.training.exercise.request.CreateExerciseRequest;
import pm.workout.helper.domain.training.excercise.Exercise;
import pm.workout.helper.domain.training.excercise.ExerciseRepository;
import pm.workout.helper.domain.training.excercise.ExerciseType;
import pm.workout.helper.domain.training.excercise.TargetMuscle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
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
public class ExerciseRestResourceItTest extends AbstractItTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String baseURL = "/training-plan/api/exercises";

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
    }

    @Autowired
    ExerciseRepository exerciseRepository;

    @Test
    void shouldSaveExercise() throws Exception {
        //given:
        CreateExerciseRequest request = new CreateExerciseRequest("test", "desc", "url", ExerciseType.COMPOUNDED, TargetMuscle.CHEST, List.of());

        //when:
        mockMvc.perform(post(baseURL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                //then:
                .andExpect(status().isOk());

        Exercise exercise = exerciseRepository.getAllExercises().get(0);
        assert (exercise.getName().equals("test"));
        assert (exercise.getDescription().equals("desc"));
        assert (exercise.getVideoUrl().equals("url"));
        assert (exercise.getTargetMuscle().equals(TargetMuscle.CHEST));
        assert (exercise.getExerciseType().equals(ExerciseType.COMPOUNDED));

    }

    @Test
    void shouldReturnValidExercises() throws Exception {
        //given:
        Exercise exercise = Exercise.builder().name("test")
                .description("desc")
                .additionalMuscles(Set.of(TargetMuscle.ABS))
                .exerciseType(ExerciseType.COMPOUNDED)
                .targetMuscle(TargetMuscle.CHEST)
                .videoUrl("url")
                .build();

        exerciseRepository.addExercise(exercise);
        Exercise savedEx = exerciseRepository.getAllExercises().get(0);

        //when:
        mockMvc.perform(get(baseURL+"?targetMuscle=CHEST")
                        .contentType(APPLICATION_JSON))
                //then:
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].exerciseId").value(savedEx.getId()))
                .andExpect(jsonPath("$[0].exerciseName").value("test"))
                .andExpect(jsonPath("$[0].exerciseDescription").value("desc"))
                .andExpect(jsonPath("$[0].videoUrl").value("url"))
                .andExpect(jsonPath("$[0].targetMuscle").value("CHEST"));
    }
}

