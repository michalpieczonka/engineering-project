package pm.workout.helper.domain.workout;

import pm.workout.helper.domain.workout.dto.WorkoutStatisticsDto;

import java.util.List;

public interface UserWorkoutService {
    List<WorkoutStatisticsDto> getTrainingUnitWorkoutStatistics(String userId, String trainingUnitId);
}
