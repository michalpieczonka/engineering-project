package pm.workout.helper.domain.workout

import pm.workout.helper.api.workout.request.SaveSeriesRepetitionsRequest
import pm.workout.helper.api.workout.request.SaveUserWorkoutRequest
import pm.workout.helper.api.workout.request.SaveWorkoutPartsRequest
import pm.workout.helper.domain.workout.doc.TrainingDay
import pm.workout.helper.domain.workout.doc.UserWorkout
import pm.workout.helper.domain.workout.doc.Workout
import pm.workout.helper.domain.workout.doc.WorkoutAssessment
import pm.workout.helper.domain.workout.doc.WorkoutPart
import spock.lang.Ignore
import spock.lang.Specification
import org.mapstruct.factory.Mappers

import java.time.LocalDateTime
import java.util.stream.Collectors

class UserWorkoutServiceImplTest extends Specification {
    def "when trying to save valid request should save user workout"() {
        given:
        final String USER_ID = "1"
        def userWorkoutRepository = new UserWorkoutRepositoryMock(List.of())
        def workoutMapper = Mock(UserWorkoutMapper)
        def userWorkoutService = new UserWorkoutServiceImpl(userWorkoutRepository, workoutMapper)
        def saveRequest = buildWorkoutSaveRequest("1", "2")

        when:
        userWorkoutService.saveUserWorkoutSession(USER_ID, saveRequest)

        then:
        userWorkoutRepository.getUserWorkouts(USER_ID).get().getPerformedWorkouts().size() == 1
        def savedWorkout = userWorkoutRepository.getUserWorkouts(USER_ID).get().getPerformedWorkouts().get(0)
        savedWorkout.trainingPlanId == 1
        savedWorkout.trainingUnitId == 2
        savedWorkout.workoutAssessment.additionalComment == "comment"
        savedWorkout.workoutAssessment.personalRate == 6
        savedWorkout.trainingDay == TrainingDay.FRIDAY
        savedWorkout.workoutParts.size() == 1
    }

    def "when trying to get all user workouts should return all valid workouts sorted by finish time"() {
        given:
        final String USER_ID = "1"
        def w1 = buildWorkout("1", 1l, 1l, LocalDateTime.now().minusDays(1), List.of() )
        def w2 = buildWorkout("2", 1l, 1l, LocalDateTime.now().minusDays(3), List.of() )

        def userWorkoutRepository = new UserWorkoutRepositoryMock(List.of(new UserWorkout(USER_ID, List.of(w1, w2))))
        def workoutMapper = new UserWorkoutMapper()
        def userWorkoutService = new UserWorkoutServiceImpl(userWorkoutRepository, workoutMapper)

        when:
        def result = userWorkoutService.getAllUserWorkouts(USER_ID)

        then:
        result.size() == 2
        result.get(0).workoutId == "1"
        result.get(1).workoutId == "2"
    }

    def "when trying to get latest user workout should return valid one"() {
        given:
        final String USER_ID = "1"
        def w1 = buildWorkout("1", 1l, 1l, LocalDateTime.now().minusDays(1), List.of() )
        def w2 = buildWorkout("2", 1l, 1l, LocalDateTime.now().minusDays(3), List.of() )

        def userWorkoutRepository = new UserWorkoutRepositoryMock(List.of(new UserWorkout(USER_ID, List.of(w1, w2))))
        def workoutMapper = new UserWorkoutMapper()
        def userWorkoutService = new UserWorkoutServiceImpl(userWorkoutRepository, workoutMapper)

        when:
        def result = userWorkoutService.getLatestUserWorkout(USER_ID, "1")

        then:
        result.workoutId == "1"
        result.trainingPlanId == 1l
        result.trainingDay == TrainingDay.FRIDAY
        result.workoutAssessment.personalRate == 6
        result.workoutAssessment.additionalComment == "comment"
    }

    @Ignore
    def "when trying to delete user workout, should delete valid workout"() {
        given:
        final String USER_ID = "1"
        def w1 = buildWorkout("1", 1l, 1l, LocalDateTime.now().minusDays(1), List.of() )
        def w2 = buildWorkout("2", 1l, 1l, LocalDateTime.now().minusDays(3), List.of() )

        def userWorkoutRepository = new UserWorkoutRepositoryMock(Arrays.asList(new UserWorkout(USER_ID, Arrays.asList(w1, w2))))
        def workoutMapper = new UserWorkoutMapper()
        def userWorkoutService = new UserWorkoutServiceImpl(userWorkoutRepository, workoutMapper)

        when:
        userWorkoutService.deleteUserWorkout("1")

        then:
        userWorkoutRepository.getUserWorkouts(USER_ID).get().getPerformedWorkouts().size() == 1
    }

    def "when trying to get volume one rep max statistics should return valid values"() {
        given:
        final String USER_ID = "1"
        def w1 = buildWorkout("1", 1l, 1l, LocalDateTime.now().minusDays(1), List.of() )
        def w2 = buildWorkout("2", 1l, 1l, LocalDateTime.now().minusDays(3), List.of() )

        def userWorkoutRepository = new UserWorkoutRepositoryMock(Arrays.asList(new UserWorkout(USER_ID, Arrays.asList(w1, w2))))
        def workoutMapper = new UserWorkoutMapper()
        def userWorkoutService = new UserWorkoutServiceImpl(userWorkoutRepository, workoutMapper)

        when:
        def response = userWorkoutService.getVolumeOneRepMaxStatistics(USER_ID, "1")

        then:
        response.size() == 2
    }


    def "when trying to get dashboard statisticsc, should return valid values for each statistic"() {
        given:
        final String USER_ID = "1"
        def w1 = buildWorkout("1", 1l, 1l, LocalDateTime.now().plusMinutes(34), List.of() )
        def w2 = buildWorkout("2", 1l, 1l, LocalDateTime.now().plusMinutes(90), List.of() )

        def userWorkoutRepository = new UserWorkoutRepositoryMock(Arrays.asList(new UserWorkout(USER_ID, Arrays.asList(w1, w2))))
        def workoutMapper = new UserWorkoutMapper()
        def userWorkoutService = new UserWorkoutServiceImpl(userWorkoutRepository, workoutMapper)

        when:
        def response = userWorkoutService.getDashboardStatistics(USER_ID)

        then:
        response.totalWorkouts == 2
        response.totalTrainingPlans == 2
        response.totalTrainingMinutes >= 119
        response.averageTrainingRate == 6
        response.averageTrainingTime == 62
        response.workoutsPerPlan.size() == 1
    }

    private SaveUserWorkoutRequest buildWorkoutSaveRequest(String trainingPlanId, trainingUnitId){
       return new SaveUserWorkoutRequest(
               trainingPlanId,
               trainingUnitId,
               LocalDateTime.now(),
               LocalDateTime.now(),
               TrainingDay.FRIDAY,
               List.of(buildSaveWorkoutPartsRequest()),
               new WorkoutAssessment("comment", 6)
       )
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
        )
    }

    private Workout buildWorkout(String workoutId = "1", long trainingPlanId = 1L, long trainingUnitId = 1L, LocalDateTime finishedAt, List<WorkoutPart> workoutParts = List.of()){
        return Workout.builder()
        .workoutId(workoutId)
        .trainingPlanId(trainingPlanId)
        .trainingUnitId(trainingUnitId)
        .startedAt(LocalDateTime.now())
        .finishedAt(finishedAt)
        .trainingDay(TrainingDay.FRIDAY)
        .workoutParts(workoutParts)
        .workoutAssessment(new WorkoutAssessment("comment", 6))
                .build()

    }

    private final class UserWorkoutRepositoryMock implements  UserWorkoutRepository {
        private final Map<String, List<Workout>> USER_WORKOUTS = new HashMap<>()

        UserWorkoutRepositoryMock(List<UserWorkout> workouts){
            workouts.each { userWorkout ->
                USER_WORKOUTS.put(userWorkout.userId, userWorkout.performedWorkouts)
            }
        }

        @SuppressWarnings("GroovyAccessibility")
        @Override
        void saveUserWorkout(String userId, Workout workout) {
            List<Workout> workouts = Optional.ofNullable(USER_WORKOUTS.get(userId)).orElse(new ArrayList<Workout>())
            workouts.add(workout)
            USER_WORKOUTS.put(userId, workouts)
        }

        @Override
        Optional<UserWorkout> getUserWorkouts(String userId) {
           return  Optional.of(new UserWorkout(userId, USER_WORKOUTS.get(userId)));
        }

        @Override
        Optional<Workout> getWorkout(String workoutId) {
            List<Workout> workouts = USER_WORKOUTS.values().stream().flatMap(Collection::stream).collect(Collectors.toList())
            return workouts.stream().filter(workout -> workout.workoutId == workoutId).findFirst()
        }

        @Override
        void saveWorkout(UserWorkout userWorkout) {
            USER_WORKOUTS.put(userWorkout.userId, userWorkout.performedWorkouts)
        }

        @Override
        Optional<UserWorkout> findUserWorkoutsByWorkoutId(String workoutId) {
            List<Workout> workouts = USER_WORKOUTS.values().stream().flatMap(Collection::stream).collect(Collectors.toList())
            var res = workouts.stream().filter(workout -> workout.workoutId == workoutId).findFirst()
            if (res.isEmpty())
                return Optional.empty()
            else
                return Optional.of(new UserWorkout("1", List.of(res.get())))
        }
    }
}
