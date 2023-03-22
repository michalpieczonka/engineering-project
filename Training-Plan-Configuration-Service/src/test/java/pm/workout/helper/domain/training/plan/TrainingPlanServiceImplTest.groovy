package pm.workout.helper.domain.training.plan

import org.junit.Ignore
import org.junit.jupiter.api.Disabled
import org.mapstruct.factory.Mappers
import pm.workout.helper.api.training.plan.request.CreateTrainingPlanRequest
import pm.workout.helper.api.training.plan.request.CreateTrainingUnitPartRequest
import pm.workout.helper.api.training.plan.request.CreateTrainingUnitRequest
import pm.workout.helper.api.training.plan.request.UnitPartExerciseDetails
import pm.workout.helper.api.training.plan.request.UpdateTrainingPlanRequest
import pm.workout.helper.api.training.plan.request.UpdateTrainingUnitPartRequest
import pm.workout.helper.api.training.plan.request.plan.AddTrainingPlanRateRequest
import pm.workout.helper.domain.training.exception.TrainingPlanNotFoundException
import pm.workout.helper.domain.training.excercise.Exercise
import pm.workout.helper.domain.training.excercise.ExerciseRepository
import pm.workout.helper.domain.training.excercise.ExerciseType
import pm.workout.helper.domain.training.excercise.TargetMuscle
import pm.workout.helper.domain.training.excercise.exception.ExerciseNotFoundException
import pm.workout.helper.domain.training.plan.dto.SeriesRepetitionsDto
import pm.workout.helper.domain.training.plan.rate.TrainingPlanRate
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors

class TrainingPlanServiceImplTest extends Specification {

    def "when trying to get all user training plan details should return plan details"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)

        when:
        def response = trainingPlanService.getAllUserTrainingPlans(1L)

        then:
        response.size() == 1
        response.get(0).id == 1L
        response.get(0).title == "test"
        !response.get(0).isPublic
    }

    def "when trying to get training plan details should return plan details"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)

        when:
        def response = trainingPlanService.getTrainingPlanDetails(1L)

        then:
        response.id == 1L
        response.title == "test"
        !response.isPublic
    }

    def "when trying to get training plan details should and plan not exists should throw"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)

        when:
        def response = trainingPlanService.getTrainingPlanDetails(3L)

        then:
        thrown TrainingPlanNotFoundException
    }

    def "when trying to create new training plan should create one"() {
        given:
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock()
        ExerciseRepository exerciseRepository = Stub()
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        exerciseRepository.getExerciseById(1L) >> new Exercise(1L, "name", "desc", "url", TargetMuscle.BACK_DELTOIDS, ExerciseType.COMPOUNDED, Set.of(), Set.of())
        def createRequest = createTrainingPlanRequest("test", false, List.of())

        when:
        def response = trainingPlanService.createNewTrainingPlan(createRequest, 1L)

        then:
        response == 2
        trainingPlanRepository.getAllUserTrainingPlans(1L).size() == 1
    }

    @spock.lang.Ignore
    def "when trying to create new training plan for not existing exercise should throw"() {
        given:
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock()
        ExerciseRepository exerciseRepository = Stub()
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        exerciseRepository.getExerciseById(5L) >> new ExerciseNotFoundException(5L)
        def createRequest = createTrainingPlanRequest("test", false, List.of())

        when:
        trainingPlanService.createNewTrainingPlan(createRequest, 1L)

        then:
        thrown ExerciseNotFoundException
    }

    def "when trying to update plan that not exists should throw"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        def request = new UpdateTrainingPlanRequest("test2", "desc2", Set.of(), PlanPriority.ARMS, TrainingPlanType.FULL_BODY_WORKOUT, false)

        when:
        trainingPlanService.updateTrainingPlan(3L, request)

        then:
        thrown TrainingPlanNotFoundException
    }

    def "when trying to update plan for existing plan should update it"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        def request = new UpdateTrainingPlanRequest("test2", "desc2", Set.of(), PlanPriority.ARMS, TrainingPlanType.FULL_BODY_WORKOUT, false)

        when:
        trainingPlanService.updateTrainingPlan(1L, request)

        then:
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).title == "test2"
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).description == "desc2"
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).planPriority == PlanPriority.ARMS
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).type == TrainingPlanType.FULL_BODY_WORKOUT
        !trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).isPublic
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).trainingUnits.size() == 0
    }


    def "when trying to get training unit part template should return valid template"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L), Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of())))))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        def request = new UpdateTrainingPlanRequest("test2", "desc2", Set.of(), PlanPriority.ARMS, TrainingPlanType.FULL_BODY_WORKOUT, false)

        when:
        def response = trainingPlanService.getTrainingUnitPartTemplate(1L, TrainingDay.FRIDAY)

        then:
        response.trainingUnitId == 1L
        response.trainingPlanId == 1L
        response.trainingDay == TrainingDay.FRIDAY
        response.trainingUnitParts.size() == 1
    }

    def "when trying to get training unit part template for not existing training day should throw"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L), Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of())))))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        def request = new UpdateTrainingPlanRequest("test2", "desc2", Set.of(), PlanPriority.ARMS, TrainingPlanType.FULL_BODY_WORKOUT, false)

        when:
        trainingPlanService.getTrainingUnitPartTemplate(1L, TrainingDay.SATURDAY)

        then:
        thrown TrainingPlanNotFoundException
    }

    def "when trying to get training unit part template for not existing training plan should throw"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L), Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of())))))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        def request = new UpdateTrainingPlanRequest("test2", "desc2", Set.of(), PlanPriority.ARMS, TrainingPlanType.FULL_BODY_WORKOUT, false)

        when:
        trainingPlanService.getTrainingUnitPartTemplate(3L, TrainingDay.FRIDAY)

        then:
        thrown TrainingPlanNotFoundException
    }

    @spock.lang.Ignore
    def "when trying to update training unit part for existing  plan should update unit part"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L), Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of(1, 1))))))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        def request =  new UpdateTrainingUnitPartRequest(Set.of(
                new SeriesRepetitionsDto(
                1, 10)))

        when:
        trainingPlanService.updateTrainingUnitPart(1L, 1L, 1L, request)

        then:
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).title == "test2"
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).description == "desc2"
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).planPriority == PlanPriority.ARMS
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).type == TrainingPlanType.FULL_BODY_WORKOUT
        !trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).isPublic
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).trainingUnits.size() == 1
    }

    def "when trying to add update training unit part for not existing  plan should throw"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        def request = new UpdateTrainingPlanRequest("test2", "desc2", Set.of(), PlanPriority.ARMS, TrainingPlanType.FULL_BODY_WORKOUT, false)

        when:
        trainingPlanService.updateTrainingUnitPart(1L,1L,1L, request)

        then:
        thrown Exception
    }

    def "when trying to add training unit part for existing  plan should add unit part"() {
        given:
        def partSet = [buildPart(1L, null, Map.of(1, 1))] as Set
        def trainSet = [buildTrainingUnit(1L, null, TrainingDay.FRIDAY, partSet)] as Set
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L), trainSet)
      //  def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L), Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of(1, 1))))))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        ExerciseRepository exerciseRepository = Stub()
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        exerciseRepository.getExerciseById(5L) >> Optional.of(new Exercise(1L, "name", "desc", "url", TargetMuscle.BACK_DELTOIDS, ExerciseType.COMPOUNDED, Set.of(), Set.of()))
        def request = new CreateTrainingUnitPartRequest(5L, List.of(new UnitPartExerciseDetails(1, 10)))

        when:
        trainingPlanService.addTrainingUnitPart(1L, 1L,  request)

        then:
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).title == "test"
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).description == "desc"
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).planPriority == PlanPriority.CHEST
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).type == TrainingPlanType.FULL_BODY_WORKOUT
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).isPublic
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).trainingUnits.size() == 1
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).trainingUnits.stream().filter(p -> p.getUnitParts().size() == 2).count() == 1
    }

    def "when trying to add training unit part with not existing exercise should throw "() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L), Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of(1, 1))))))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        def request = new CreateTrainingUnitPartRequest(1L, List.of(new UnitPartExerciseDetails(1, 10)))

        when:
        trainingPlanService.addTrainingUnitPart(1L, 1L,  request)

        then:
        thrown Exception
    }

    def "when trying to get all public plans should return all valid plans"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, "test", true, Set.of(1L), Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of(1, 1))))))
        def privatePlan = buildTrainingPlan(2L, "test2", false, Set.of(1L), Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of(1, 1))))))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan, privatePlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)

        when:
        def response = trainingPlanService.getAllPublicTrainingPlans(1L)

        then:
        response.size() == 1
        response.get(0).id == 1L
        response.get(0).isPublic()
        response.get(0).title == "test"
    }

    def "when trying to copy training plan and trianing plan exists should copy plan and add to valid user"() {
        given:
        def planUsers = [1L] as Set
        def trainingPlan = buildTrainingPlan(1L, "test", true, planUsers, Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of(1, 1))))))
         def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)

        when:
        trainingPlanService.copyTrainingPlan(1L, 2L)

        then:
        trainingPlanRepository.getAllUserTrainingPlans(2L).size() == 2
        trainingPlanRepository.getAllUserTrainingPlans(2L).stream().filter(d -> d.title.contains("kopia"))
        def copiedPlan = trainingPlanRepository.getAllUserTrainingPlans(2L).get(0)
        def originalPlan = trainingPlanRepository.getAllUserTrainingPlans(1L).get(0)
        copiedPlan.trainingUnits.size() == originalPlan.trainingUnits.size()
        copiedPlan.type == originalPlan.type
        copiedPlan.planPriority == originalPlan.planPriority
        copiedPlan.description == originalPlan.description
        copiedPlan.isPublic == originalPlan.isPublic
        copiedPlan.trainingUnits.unitParts.size() == originalPlan.trainingUnits.unitParts.size()
    }

    def "when trying to copy training plan and training plan not exists should throw"() {
        given:
        def planUsers = [1L] as Set
        def trainingPlan = buildTrainingPlan(1L, "test", true, planUsers, Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of(1, 1))))))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)

        when:
        trainingPlanService.copyTrainingPlan(5L, 2L)

        then:
        thrown TrainingPlanNotFoundException
    }


    def "when trying to add training rate and trianing plan exists should add rate"() {
        def planUsers = [1L] as Set
        def trainingPlan = buildTrainingPlan(1L, "test", true, planUsers, Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of(1, 1))))))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        def request = new AddTrainingPlanRateRequest(1L, 1, "test")

        when:
        trainingPlanService.addTrainingPlanRate(1L, request)

        then:
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).planRates.size() == 1
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).planRates.stream().findFirst().get().rate == 1
        trainingPlanRepository.getAllUserTrainingPlans(1L).get(0).planRates.stream().findFirst().get().description == "test"

    }

    def "when trying add training rate for not existing training plan should throw"() {
        given:
        def planUsers = [1L] as Set
        def trainingPlan = buildTrainingPlan(1L, "test", true, planUsers, Set.of(buildTrainingUnit(1L, null, TrainingDay.FRIDAY, Set.of(buildPart(1L, null, Map.of(1, 1))))))
        def trainingPlanMapper = Mappers.getMapper(TrainingPlanMapper.class)
        def trainingPlanRepository = new TrainingPlanRepositoryMock(List.of(trainingPlan))
        def exerciseRepository = Mock(ExerciseRepository)
        def trainingPlanFactory = new TrainingPlanFactory(exerciseRepository)
        def trainingPlanService = new TrainingPlanServiceImpl(trainingPlanRepository, trainingPlanMapper, trainingPlanFactory)
        def request = new AddTrainingPlanRateRequest(1L, 1, "test")

        when:
        trainingPlanService.addTrainingPlanRate(5L, request)

        then:
        thrown TrainingPlanNotFoundException
    }

    class TrainingPlanRepositoryMock implements TrainingPlanRepository {
        private final Map<Long, TrainingPlan> TRAINING_PLANS_MAP = new HashMap<>();
        private AtomicLong seq = new AtomicLong(1L)

        TrainingPlanRepositoryMock() {}

        TrainingPlanRepositoryMock(List<TrainingPlan> trainingPlans) {
            trainingPlans.forEach { plan ->
                TRAINING_PLANS_MAP.put(seq.getAndIncrement(), plan)
            }
        }

        @Override
        List<TrainingPlan> getAllUserTrainingPlans(Long userId) {
            return TRAINING_PLANS_MAP.values().stream().filter(p -> p.getPlanUsersIds().contains(userId)).collect(Collectors.toList())
        }

        @Override
        Optional<TrainingPlan> getTrainingPlanDetails(Long trainingPlanId) {
            return Optional.ofNullable(TRAINING_PLANS_MAP.get(trainingPlanId))
        }

        @Override
        TrainingPlan createNewTrainingPlan(TrainingPlan trainingPlan) {
            def planId = seq.incrementAndGet()
            trainingPlan.id = planId
            TRAINING_PLANS_MAP.put(planId, trainingPlan)
            return TRAINING_PLANS_MAP.get(planId)
        }

        @Override
        void deleteTrainingPlan(Long trainingPlanId) {
            TRAINING_PLANS_MAP.remove(trainingPlanId)
        }

        @Override
        Optional<TrainingUnit> getTrainingUnit(Long trainingPlanId, TrainingDay trainingDay) {
            def trainnigPlan = TRAINING_PLANS_MAP.values().stream().filter(p -> p.getId() == trainingPlanId)
                    .findFirst()
            if (trainnigPlan.isEmpty())
                return Optional.empty()
            else
                return trainnigPlan.get().getTrainingUnits().stream().filter(p -> p.getTrainingDay() == trainingDay).findAny()
        }

        @Override
        Optional<TrainingPlan> getTrainingPlanById(Long trainingPlanId) {
            return Optional.ofNullable(TRAINING_PLANS_MAP.get(trainingPlanId))
        }

        @Override
        void updateTrainingPlan(TrainingPlan trainingPlan) {
            TRAINING_PLANS_MAP.put(trainingPlan.getId(), trainingPlan)
        }

        @Override
        void updateTrainingUnit(TrainingUnit trainingUnit) {
            TRAINING_PLANS_MAP.values().stream().filter(plan -> plan.getTrainingUnits().contains(trainingUnit)).findFirst().ifPresent(plan -> {
                plan.getTrainingUnits().remove(trainingUnit)
                plan.getTrainingUnits().add(trainingUnit)
            })
        }

        @Override
        List<TrainingPlan> getAllPublicTrainingPlans() {
            return TRAINING_PLANS_MAP.values().stream().filter { it.public }.collect(Collectors.toList())
        }

        @Override
        void createNewTrainingPlanCopy(TrainingPlan plan, Set<TrainingUnit> baseUnits) {
            TRAINING_PLANS_MAP.put(seq.getAndIncrement(), plan)
        }
    }

    private TrainingPlan buildTrainingPlan(Long id, String title, boolean isPublic, Set<Long> planUsersIds = Set.of(), Set<TrainingUnit> trainingUnits = Set.of(), Set<TrainingPlanRate> planRates = [] as Set) {
        return TrainingPlan
                .builder()
                .id(id)
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
                .build()
    }

    private TrainingUnit buildTrainingUnit(Long id, TrainingPlan plan, TrainingDay day, Set<TrainingUnitPart> trainingUnitParts = Set.of()) {
        return TrainingUnit
                .builder()
                .id(id)
                .trainingDay(day)
                .trainingPlan(plan)
                .unitParts(trainingUnitParts)
                .build()
    }

    private TrainingUnitPart buildPart(Long id, TrainingUnit unit, Map<Integer, Integer> seriesRepetitionsMap = Map.of()) {
        return TrainingUnitPart
                .builder()
                .id(id)
                .trainingUnit(unit)
                .exercise(Exercise.builder()
                        .id(1L)
                        .name("exercise")
                        .build()
                )
                .seriesRepetitionsMap(seriesRepetitionsMap)
                .build()
    }

    private TrainingPlanRate buildRate(Long id, String comment, Integer value) {
        return TrainingPlanRate
                .builder()
                .id(id)
                .description(comment)
                .rate(value)
                .build();
    }

    private CreateTrainingPlanRequest createTrainingPlanRequest(String title, boolean isPublic, List<CreateTrainingUnitRequest> createTrainingUnitRequest = List.of()) {
        return CreateTrainingPlanRequest.builder()
                .title(title)
                .description("ez")
                .isPublic(isPublic)
                .trainingDays(List.of(TrainingDay.MONDAY))
                .planType(TrainingPlanType.FULL_BODY_WORKOUT)
                .targetFinishDate(LocalDate.now().plusDays(1))
                .trainingUnitsDetails(createTrainingUnitRequest)
                .build()
    }

    private CreateTrainingUnitRequest createTrainingUnitRequest(TrainingDay day, List<CreateTrainingUnitPartRequest> createTrainingUnitPartRequest = List.of()) {
        return CreateTrainingUnitRequest
                .builder()
                .trainingDay(day)
                .trainingUnitParts(createTrainingUnitPartRequest)
                .build()
    }

    private CreateTrainingUnitPartRequest createTrainingUnitPartRequest(List<UnitPartExerciseDetails> seriesRepetitionsDetails = List.of()) {
        return CreateTrainingUnitPartRequest.builder()
                .exerciseId(1L)
                .seriesRepetitionsDetails(seriesRepetitionsDetails)
                .build();
    }

    private UnitPartExerciseDetails details (Integer series, Integer repetitions = 12) {
        return new UnitPartExerciseDetails(series, repetitions)
    }
}
