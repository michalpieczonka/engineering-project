package pm.workout.helper.domain.workout;

import pm.workout.helper.api.workout.request.SaveUserWorkoutRequest;
import pm.workout.helper.domain.workout.dto.DashboardStatisticsDto;
import pm.workout.helper.domain.workout.dto.UserWorkoutDto;
import pm.workout.helper.domain.workout.dto.basic_statistics.VolumeExerciseDto;
import pm.workout.helper.domain.workout.dto.number_statistics.ExerciseSeriesDetailsDto;

import java.util.List;

public interface UserWorkoutService {
    void saveUserWorkoutSession(String userId, SaveUserWorkoutRequest request);
    List<UserWorkoutDto> getAllUserWorkouts(String userId);
    UserWorkoutDto getUserWorkout(String workoutId);
    UserWorkoutDto getLatestUserWorkout(String userId, String trainingUnitId);
    List<UserWorkoutDto> getAllUserWorkoutsForTrainingUnit(String userId, String trainingUnitId);
    List<UserWorkoutDto> getAllUserWorkoutsForTrainingPlan(String userId, String trainingPlanId);
    void deleteUserWorkout(String workoutId);
    List<VolumeExerciseDto> getVolumeOneRepMaxStatistics(String userId, String trainingPlanId);
    List<ExerciseSeriesDetailsDto> getExerciseSeriesDetails(String userId, String trainingPlanId);
    DashboardStatisticsDto getDashboardStatistics(String userId);

}
