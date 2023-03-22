package pm.workout.helper.api.external.workout;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pm.workout.helper.domain.workout.dto.UserWorkoutDto;
import pm.workout.helper.infrastructure.services.WorkoutService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/workouts")
public class WorkoutRestResource {
    private final WorkoutService workoutService;

    @GetMapping("/{workoutId}")
    @ResponseStatus(HttpStatus.OK)
    UserWorkoutDto getUserWorkout(@PathVariable @NotNull String workoutId){
        return workoutService.getUserWorkout(workoutId);
    }

    @GetMapping("/{userId}/workouts/training-units/{trainingUnitId}/history")
    @ResponseStatus(HttpStatus.OK)
    List<UserWorkoutDto> getUserWorkoutHistory(@PathVariable @NotNull String userId, @PathVariable @NotNull String trainingUnitId) {
        return workoutService.getAllUserWorkoutsForTrainingUnit(userId, trainingUnitId);
    }

    @GetMapping("/{userId}/workouts/training-units/{trainingUnitId}/latest")
    @ResponseStatus(HttpStatus.OK)
    UserWorkoutDto getUserLatestWorkout(@PathVariable @NotNull String userId, @PathVariable @NotNull String trainingUnitId) {
        List<UserWorkoutDto> workouts = workoutService.getAllUserWorkoutsForTrainingUnit(userId, trainingUnitId);
        LocalDateTime latestTime = workouts.stream().map(UserWorkoutDto::getFinishedAt).max(LocalDateTime::compareTo).orElse(null);
        return workouts.stream().filter(workout -> workout.getFinishedAt().equals(latestTime)).findFirst().orElse(UserWorkoutDto.getEmptyDto());
    }

    @GetMapping("/{userId}/training-plans/{trainingPlanId}/statistics")
    @ResponseStatus (HttpStatus.OK)
    List<Object> getUserVolumeOneRepMaxDetails(@PathVariable @NotNull String userId, @PathVariable @NotNull String trainingPlanId) {
        return workoutService.getUserVolumeOneRepMaxDetails(userId, trainingPlanId);
    }

    @GetMapping("/{userId}/training-plans/{trainingPlanId}/total-statistics")
    @ResponseStatus (HttpStatus.OK)
    List<Object> getUserTotalStatistics(@PathVariable @NotNull String userId, @PathVariable @NotNull String trainingPlanId) {
        return workoutService.getUserSeriesRepsStatistics(userId, trainingPlanId);
    }


}
