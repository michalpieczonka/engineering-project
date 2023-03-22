package pm.workout.helper.infrastructure.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pm.workout.helper.domain.workout.dto.UserWorkoutDto;
import pm.workout.helper.infrastructure.configuration.FeignConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(value = "user-workout-service",
        path = "/workout/api",
        configuration = FeignConfiguration.class
)
public interface WorkoutService {
    @PostMapping(path = "/workouts/users/{userId}")
    void addUserWorkoutSession(@PathVariable ("userId") @NotNull Long userId,
                               @NotNull @Valid @RequestBody Object workoutSession);

    @GetMapping(path = "/workouts/users/{userId}/latest/{trainingUnitId}")
    UserWorkoutDto getLatestUserWorkout(@PathVariable("userId") @NotNull Long userId,
                                        @PathVariable("trainingUnitId") @NotNull Long trainingUnitId);

    @GetMapping(path = "/workouts/users/{userId}")
    List<UserWorkoutDto> getAllUserWorkouts(@PathVariable("userId") @NotNull Long userId);

    @GetMapping(path = "/workouts/{workoutId}")
    UserWorkoutDto getUserWorkout(@PathVariable ("workoutId") @NotNull String workoutId);

    @GetMapping(path = "/workouts/users/{userId}/trainingUnits/{trainingUnitId}/history")
    List<UserWorkoutDto> getAllUserWorkoutsForTrainingUnit(@PathVariable("userId") @NotNull String userId,
                                                          @PathVariable("trainingUnitId") @NotNull String trainingUnitId);


    @GetMapping(path = "/workouts/users/{userId}/trainingPlans/{trainingPlanId}/history")
    List<UserWorkoutDto> getAllUserWorkoutsForTrainingPlan(@PathVariable("userId") @NotNull String userId,
                                                         @PathVariable("trainingPlanId") @NotNull String trainingPlanId);

    @GetMapping(path = "/workouts/users/{userId}/training-plan/{trainingPlanId}/statistics")
    List<Object> getUserVolumeOneRepMaxDetails(@PathVariable("userId") @NotNull String userId,
                                              @PathVariable("trainingPlanId") @NotNull String trainingPlanId);

    @GetMapping(path = "/workouts/users/{userId}/training-plan/{trainingPlanId}/total-statistics")
    List<Object> getUserSeriesRepsStatistics(@PathVariable("userId") @NotNull String userId,
                                              @PathVariable("trainingPlanId") @NotNull String trainingPlanId);

    @GetMapping(path = "workouts/users/{userId}/statistics")
    Object getUserWorkoutsBasicStatistics(@PathVariable("userId") @NotNull String userId);
}
