package pm.workout.helper.api.workout;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pm.workout.helper.api.workout.request.SaveUserWorkoutRequest;
import pm.workout.helper.domain.workout.UserWorkoutService;
import pm.workout.helper.domain.workout.dto.DashboardStatisticsDto;
import pm.workout.helper.domain.workout.dto.UserWorkoutDto;
import pm.workout.helper.domain.workout.dto.basic_statistics.VolumeExerciseDto;
import pm.workout.helper.domain.workout.dto.number_statistics.ExerciseSeriesDetailsDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/workout/api/workouts")
public class WorkoutRestResource {
    private final UserWorkoutService userWorkoutService;

    @PostMapping(path = "/users/{userId}")
    private void addUserWorkoutSession(@PathVariable("userId") @NotNull Long userId,
                                     @NotNull @Valid @RequestBody SaveUserWorkoutRequest request){
        userWorkoutService.saveUserWorkoutSession(userId.toString(), request);
    }

    @GetMapping(path = "/users/{userId}")
    private List<UserWorkoutDto> getAllUserWorkouts(@PathVariable("userId") @NotNull Long userId){
        return userWorkoutService.getAllUserWorkouts(userId.toString());
    }

    @GetMapping(path = "/users/{userId}/latest/{trainingUnitId}")
    private UserWorkoutDto getLatestUserWorkout(@PathVariable("userId") @NotNull Long userId,
                                                @PathVariable("trainingUnitId") @NotNull String trainingUnitId){
        return userWorkoutService.getLatestUserWorkout(userId.toString(), trainingUnitId);
    }

    @GetMapping(path = "/{workoutId}")
    private UserWorkoutDto getUserWorkout(@PathVariable ("workoutId") @NotNull String workoutId){
        return userWorkoutService.getUserWorkout(workoutId);
    }

    @GetMapping(path = "/users/{userId}/trainingUnits/{trainingUnitId}/history")
    private List<UserWorkoutDto> getAllUserWorkoutsForTrainingUnit(@PathVariable("userId") @NotNull String userId,
                                                                  @PathVariable("trainingUnitId") @NotNull String trainingUnitId){
        return userWorkoutService.getAllUserWorkoutsForTrainingUnit(userId, trainingUnitId);
    }

    @GetMapping(path = "/users/{userId}/trainingPlans/{trainingPlanId}/history")
    private List<UserWorkoutDto> getAllUserWorkoutsForTrainingPlan(@PathVariable("userId") @NotNull String userId,
                                                                 @PathVariable("trainingPlanId") @NotNull String trainingPlanId){
        return userWorkoutService.getAllUserWorkoutsForTrainingPlan(userId, trainingPlanId);
    }

    @DeleteMapping(path = "/{workoutId}")
    private void deleteUserWorkout(@PathVariable("workoutId") @NotNull String workoutId){
        userWorkoutService.deleteUserWorkout(workoutId);
    }

    @GetMapping(path = "/users/{userId}/training-plan/{trainingPlanId}/statistics")
    private List<VolumeExerciseDto> getUserVolumeOneRepMaxDetails(@PathVariable("userId") @NotNull String userId,
                                                                 @PathVariable("trainingPlanId") @NotNull String trainingPlanId){
        return userWorkoutService.getVolumeOneRepMaxStatistics(userId, trainingPlanId);
    }

    @GetMapping(path = "/users/{userId}/training-plan/{trainingPlanId}/total-statistics")
    private List<ExerciseSeriesDetailsDto> getUserSeriesRepsStatistics(@PathVariable("userId") @NotNull String userId,
                                                                         @PathVariable("trainingPlanId") @NotNull String trainingPlanId){
        return userWorkoutService.getExerciseSeriesDetails(userId, trainingPlanId);
    }

    @GetMapping(path = "/users/{userId}/statistics")
    private DashboardStatisticsDto getUserWorkoutsBasicStatistics(@PathVariable("userId") @NotNull String userId){
        return userWorkoutService.getDashboardStatistics(userId);
    }
}
