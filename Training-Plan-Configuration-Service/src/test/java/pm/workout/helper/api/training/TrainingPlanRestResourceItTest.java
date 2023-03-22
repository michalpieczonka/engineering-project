package pm.workout.helper.api.training;

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
import pm.workout.helper.api.training.plan.request.CreateTrainingPlanRequest;
import pm.workout.helper.api.training.plan.request.CreateTrainingUnitPartRequest;
import pm.workout.helper.api.training.plan.request.CreateTrainingUnitRequest;
import pm.workout.helper.api.training.plan.request.UnitPartExerciseDetails;
import pm.workout.helper.domain.training.excercise.Exercise;
import pm.workout.helper.domain.training.excercise.ExerciseRepository;
import pm.workout.helper.domain.training.excercise.ExerciseType;
import pm.workout.helper.domain.training.excercise.TargetMuscle;
import pm.workout.helper.domain.training.plan.PlanPriority;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.training.plan.TrainingPlan;
import pm.workout.helper.domain.training.plan.TrainingPlanRepository;
import pm.workout.helper.domain.training.plan.TrainingPlanType;
import pm.workout.helper.domain.training.plan.TrainingUnit;
import pm.workout.helper.domain.training.plan.TrainingUnitPart;
import pm.workout.helper.domain.training.plan.rate.TrainingPlanRate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
public class TrainingPlanRestResourceItTest extends AbstractItTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String baseURL = "/training-plan/api/exercises";
    private static final String plansURL = "/training-plan/api/training-plans";

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
    }

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    TrainingPlanRepository trainingPlanRepository;

    @Test
    void shouldSaveTrainingPlan() throws Exception {
        //given
        var unitPartExerciseDetails = details(1, 12);
        var unitPartDetails2 = details(2, 10);
        var unitPart = createTrainingUnitPartRequest(List.of(unitPartExerciseDetails, unitPartDetails2));
        var unit = createTrainingUnitRequest(TrainingDay.MONDAY, List.of(unitPart));
        var bodyRequest = createTrainingPlanRequest("title", false, List.of(unit));

        Exercise exercise = Exercise.builder().name("test")
                .description("desc")
                .additionalMuscles(Set.of(TargetMuscle.ABS))
                .exerciseType(ExerciseType.COMPOUNDED)
                .targetMuscle(TargetMuscle.CHEST)
                .videoUrl("url")
                .build();
        exerciseRepository.addExercise(exercise);
        Exercise savedEx = exerciseRepository.getAllExercises().get(0);

        var request = "{\"title\":\"title\",\n" +
                "\"description\":\"ez\",\n" +
                "\"trainingDays\":[\"MONDAY\"],\n" +
                "\"planPriority\": \"CHEST\",\n" +
                "\"planType\":\"FULL_BODY_WORKOUT\",\n" +
                "\"targetFinishDate\":\"2023-04-04T22:38:22\",\n" +
                "\"trainingUnitsDetails\":[{\"trainingDay\":\"MONDAY\",\n" +
                "\"trainingUnitParts\":[{\"exerciseId\":" + savedEx.getId() + ",\n" +
                "\"seriesRepetitionsDetails\":\n" +
                "[{\"seriesNumber\":1,\"numberOfRepetitions\":12},\n" +
                "{\"seriesNumber\":2,\"numberOfRepetitions\":10}]\n" +
                "}]\n" +
                "}],\n" +
                "\"public\":true}";

        mockMvc.perform(post(plansURL + "/{userId}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(request))
                //then:
                .andExpect(status().isOk());

        TrainingPlan plan = trainingPlanRepository.getAllPublicTrainingPlans().get(0);
        assert (plan.getTitle().equals("title"));
        assert (plan.getTrainingUnits().size() == 1);
        assert (plan.getDescription().equals("ez"));
        assert (plan.getPlanPriority().equals(PlanPriority.CHEST));
        assert (plan.getType().equals(TrainingPlanType.FULL_BODY_WORKOUT));
        TrainingUnit planUnit = plan.getTrainingUnits().stream().findFirst().get();
        assert (planUnit.getTrainingDay().equals(TrainingDay.MONDAY));
        assert (planUnit.getTrainingPlan().getId().equals(plan.getId()));
        assert (planUnit.getUnitParts().size() == 1);
        TrainingUnitPart unitPartT = planUnit.getUnitParts().stream().findFirst().get();
        assert (unitPartT.getTrainingUnit().getId().equals(planUnit.getId()));
        assert (unitPartT.getExercise().getId().equals(savedEx.getId()));
        assert (unitPartT.getSeriesRepetitionsMap().size() == 2);
    }

    @Test
    void shouldGetTrainingPlanThatExists() throws Exception {
        //given
        Exercise exercise = Exercise.builder().name("test")
                .description("desc")
                .additionalMuscles(Set.of(TargetMuscle.ABS))
                .exerciseType(ExerciseType.COMPOUNDED)
                .targetMuscle(TargetMuscle.CHEST)
                .videoUrl("url")
                .build();
        exerciseRepository.addExercise(exercise);
        Exercise savedEx = exerciseRepository.getAllExercises().get(0);

        TrainingUnitPart part = buildPart(savedEx, Map.of(1, 12));
        TrainingUnit unit = buildTrainingUnit(TrainingDay.MONDAY, new HashSet<>(Arrays.asList(part)));
        TrainingPlan plan = buildTrainingPlan("test", true, new HashSet<>(Arrays.asList(1L)), new HashSet<>(Arrays.asList(unit)), Set.of());
        part.setTrainingUnit(unit);
        unit.addTrainingPart(part);
        plan.addTrainingUnit(unit);
        TrainingPlan savedPlan = trainingPlanRepository.createNewTrainingPlan(plan);

        mockMvc.perform(get(plansURL + "/{trainingPlanId}", savedPlan.getId())
                        .contentType(APPLICATION_JSON))
                //then:
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedPlan.getId()))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.planDescription").value("desc"))
                .andExpect(jsonPath("$.numberOfTrainingDays").value("1"))
                .andExpect(jsonPath("$.planPriority").value("CHEST"))

                .andExpect(jsonPath("$.planCreatorUserId").value("5"))
                .andExpect(jsonPath("$.trainingUnits", hasSize(1)))
                .andExpect(jsonPath("$.planRates", hasSize(0)))
                .andExpect(jsonPath("$.planUsersIds", hasSize(1)));
    }

    @Test
    void shouldCopyTrainingPlanForExistingUser() throws Exception {
        //given
        Exercise exercise = Exercise.builder().name("test")
                .description("desc")
                .additionalMuscles(Set.of(TargetMuscle.ABS))
                .exerciseType(ExerciseType.COMPOUNDED)
                .targetMuscle(TargetMuscle.CHEST)
                .videoUrl("url")
                .build();
        exerciseRepository.addExercise(exercise);
        Exercise savedEx = exerciseRepository.getAllExercises().get(0);

        TrainingUnitPart part = buildPart(savedEx, Map.of(1, 12));
        TrainingUnit unit = buildTrainingUnit(TrainingDay.MONDAY, new HashSet<>(Arrays.asList(part)));
        TrainingPlan plan = buildTrainingPlan("test", true, new HashSet<>(Arrays.asList(1L)), new HashSet<>(Arrays.asList(unit)), Set.of());
        part.setTrainingUnit(unit);
        unit.addTrainingPart(part);
        plan.addTrainingUnit(unit);
        TrainingPlan savedPlan = trainingPlanRepository.createNewTrainingPlan(plan);

        mockMvc.perform(put(plansURL + "/{trainingPlanId}/copy/{userId}", savedPlan.getId(), 15L)
                        .contentType(APPLICATION_JSON))
                //then:
                .andExpect(status().isOk());
        var copiedPlan = trainingPlanRepository.getAllUserTrainingPlans(15L).stream().findFirst().get();
        assert(copiedPlan.getTitle().equals("test - kopia"));
        assert(copiedPlan.getPlanPriority().equals(plan.getPlanPriority()));
        assert(copiedPlan.getType().equals(plan.getType()));
    }

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


    private TrainingPlan buildTrainingPlan(String title, boolean isPublic, Set<Long> planUsersIds, Set<TrainingUnit> trainingUnits, Set<TrainingPlanRate> planRates) {
        return TrainingPlan
                .builder()
                .title(title)
                .numberOfTrainingDays(trainingUnits.size())
                .planCreatorUserId(5L)
                .description("desc")
                .planPriority(PlanPriority.CHEST)
                .type(TrainingPlanType.FULL_BODY_WORKOUT)
                .isPublic(isPublic)
                .planUsersIds(planUsersIds)
                .planRates(planRates)
                .trainingUnits(trainingUnits)
                .targetFinishDate(LocalDate.now().plusDays(1))
                .creationDate(LocalDateTime.now().minusDays(3))
                .build();
    }

    private TrainingUnit buildTrainingUnit(TrainingDay day, Set<TrainingUnitPart> trainingUnitParts) {
        return TrainingUnit
                .builder()
                .trainingDay(day)
                .unitParts(trainingUnitParts)
                .build();
    }

    private TrainingUnitPart buildPart(Exercise exercise, Map<Integer, Integer> seriesRepetitionsMap) {
        return TrainingUnitPart
                .builder()
                .exercise(exercise)
                .seriesRepetitionsMap(seriesRepetitionsMap)
                .build();
    }

    private TrainingPlanRate buildRate(Long id, String comment, Integer value) {
        return TrainingPlanRate
                .builder()
                .id(id)
                .description(comment)
                .rate(value)
                .build();
    }

    private CreateTrainingPlanRequest createTrainingPlanRequest(String title, boolean isPublic, List<CreateTrainingUnitRequest> createTrainingUnitRequest) {
        return CreateTrainingPlanRequest.builder()
                .title(title)
                .description("ez")
                .isPublic(isPublic)
                .trainingDays(List.of(TrainingDay.MONDAY))
                .planType(TrainingPlanType.FULL_BODY_WORKOUT)
                .targetFinishDate(LocalDate.now().plusDays(1))
                .trainingUnitsDetails(createTrainingUnitRequest)
                .build();
    }

    private CreateTrainingUnitRequest createTrainingUnitRequest(TrainingDay day, List<CreateTrainingUnitPartRequest> createTrainingUnitPartRequest) {
        return CreateTrainingUnitRequest
                .builder()
                .trainingDay(day)
                .trainingUnitParts(createTrainingUnitPartRequest)
                .build();
    }

    private CreateTrainingUnitPartRequest createTrainingUnitPartRequest(List<UnitPartExerciseDetails> seriesRepetitionsDetails) {
        return CreateTrainingUnitPartRequest.builder()
                .exerciseId(1L)
                .seriesRepetitionsDetails(seriesRepetitionsDetails)
                .build();
    }

    private UnitPartExerciseDetails details(Integer series, Integer repetitions) {
        return new UnitPartExerciseDetails(series, repetitions);
    }
}
