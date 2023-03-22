package pm.workout.helper.api.workout;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pm.workout.helper.api.workout.request.SaveSeriesRepetitionsRequest;
import pm.workout.helper.api.workout.request.SaveUserWorkoutRequest;
import pm.workout.helper.api.workout.request.SaveWorkoutPartsRequest;
import pm.workout.helper.domain.workout.UserWorkoutRepository;
import pm.workout.helper.domain.workout.doc.TrainingDay;
import pm.workout.helper.domain.workout.doc.UserWorkout;
import pm.workout.helper.domain.workout.doc.Workout;
import pm.workout.helper.domain.workout.doc.WorkoutAssessment;
import pm.workout.helper.domain.workout.doc.WorkoutPart;
import pm.workout.helper.infrastructure.repository.workout.UserWorkoutDbRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import(AbstractMongoContainer.class)
public class WorkoutRestResourceItTest {
    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
    }

    @Autowired
    private UserWorkoutDbRepository userWorkoutRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void cleanUp() {
        this.userWorkoutRepository.deleteAll();
    }

    private static final String baseURL = "/workout/api/workouts";

    @Test
    public void shouldSaveUserWorkoutSession() throws Exception {
        //given:
        var request = """
                {"trainingPlanId":"1",
                "trainingUnitId":"1",
                "startedAt": "2023-02-04T21:38:22",
                "finishedAt": "2023-02-04T22:38:22",
                "trainingDay":"FRIDAY",
                "workoutParts":[{"trainingUnitPartId":1,"exerciseId":1,"exerciseName":"List","seriesRepetitionsDetails":[{"seriesNumber":1,"performedRepetitionsNumber":10,"targetSeriesRepetitionsNumber":10,"usedWeight":60.0},{"seriesNumbe":2,"performedRepetitionsNumber":8,"targetSeriesRepetitionsNumber":10,"usedWeight":60.0},{"seriesNumber":3,"performedRepetitionsNumber":6,"targetSeriesRepetitionsNumber":10,"usedWeight":60.0}]}],"workoutAssessment":{"additionalComment":"comment","personalRate":6}}
                """;
//        //when:
        mockMvc.perform(post(baseURL+"/users/{userId}", "1")
                .contentType(APPLICATION_JSON)
                .content(request))
                //then
                .andExpect(status().isOk());

        var savedSession = userWorkoutRepository.findAll().get(0);
        assert(Objects.equals(savedSession.getUserId(), "1"));
        assert(savedSession.getPerformedWorkouts().size() == 1);


    }

    @Test
    void shouldReturnUserWorkout() throws Exception {
        var workout = new UserWorkout("1", List.of(buildWorkout("1", 1L, 1L, LocalDateTime.now().minusDays(1), List.of())));
        userWorkoutRepository.save(workout);
        //when:
        mockMvc.perform(get(baseURL+"/{workoutId}", "1")
                .contentType(APPLICATION_JSON))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workoutId").value("1"))
                .andExpect(jsonPath("$.trainingUnitId").value("1"))
                .andExpect(jsonPath("$.trainingDay").value("FRIDAY"))
                .andExpect(jsonPath("$.workoutAssessment.additionalComment").value("comment"))
                .andExpect(jsonPath("$.workoutAssessment.personalRate").value("6"))
                .andExpect(jsonPath("$.workoutParts", hasSize(0)))
                .andExpect(jsonPath("$.trainingPlanId").value("1"));

    }

    @Test
    void shouldDeleteValidWorkout() throws Exception {
        var workout = new UserWorkout("1", List.of(buildWorkout("1", 1L, 1L, LocalDateTime.now().minusDays(1), List.of())));
        userWorkoutRepository.save(workout);
        //when:
        mockMvc.perform(delete(baseURL+"/{workoutId}", "1")
                        .contentType(APPLICATION_JSON))
                //then
                .andDo(print())
                .andExpect(status().isOk());

        assert(userWorkoutRepository.findAll().get(0).getPerformedWorkouts().size() == 0);
    }

    @Test
    void shouldNotDeleteWorkoutAndReturnNotFound() throws Exception {
        var workout = new UserWorkout("1", List.of(buildWorkout("1", 1L, 1L, LocalDateTime.now().minusDays(1), List.of())));
        userWorkoutRepository.save(workout);
        //when:
        mockMvc.perform(delete(baseURL+"/{workoutId}", "5")
                        .contentType(APPLICATION_JSON))
                //then
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUserWorkoutStatistics() throws Exception {
        var workout = new UserWorkout("1", List.of(buildWorkout("1", 1L, 1L, LocalDateTime.now().minusDays(1), List.of())));
        userWorkoutRepository.save(workout);
        //when:
        mockMvc.perform(get(baseURL+"/users/{userId}/statistics", "1")
                        .contentType(APPLICATION_JSON))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalWorkouts").value("1"))
                .andExpect(jsonPath("$.totalTrainingPlans").value("1"))
                .andExpect(jsonPath("$.totalTrainingMinutes").value("-1440"))
                .andExpect(jsonPath("$.averageTrainingTime").value("-1440"))
                .andExpect(jsonPath("$.averageTrainingRate").value("6.0"))
                .andExpect(jsonPath("$.workoutsPerPlan.[0].trainingPlanId").value("1"))
                .andExpect(jsonPath("$.workoutsPerPlan.[0].totalWorkouts").value("1"));

    }

    private Workout buildWorkout(String workoutId , long trainingPlanId , long trainingUnitId , LocalDateTime finishedAt, List<WorkoutPart> workoutParts ){
        return Workout.builder()
                .workoutId(workoutId)
                .trainingPlanId(trainingPlanId)
                .trainingUnitId(trainingUnitId)
                .startedAt(LocalDateTime.now())
                .finishedAt(finishedAt)
                .trainingDay(TrainingDay.FRIDAY)
                .workoutParts(workoutParts)
                .workoutAssessment(new WorkoutAssessment("comment", 6))
                .build();
    }


    private SaveUserWorkoutRequest buildWorkoutSaveRequest(String trainingPlanId, String trainingUnitId){
        return new SaveUserWorkoutRequest(
                trainingPlanId,
                trainingUnitId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                TrainingDay.FRIDAY,
                List.of(buildSaveWorkoutPartsRequest()),
                new WorkoutAssessment("comment", 6)
        );
    }

    private static SaveWorkoutPartsRequest buildSaveWorkoutPartsRequest(){
        return new SaveWorkoutPartsRequest(
                1L,
                1L,
                "List",
                List.of(
                        new SaveSeriesRepetitionsRequest(1, 10, 10, 60),
                        new SaveSeriesRepetitionsRequest(2, 8, 10, 60),
                        new SaveSeriesRepetitionsRequest(3, 6, 10, 60)
                )
        );
    }



}
