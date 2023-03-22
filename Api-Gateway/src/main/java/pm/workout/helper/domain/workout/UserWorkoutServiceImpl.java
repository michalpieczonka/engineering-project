package pm.workout.helper.domain.workout;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pm.workout.helper.domain.workout.dto.WorkoutStatisticsDto;
import pm.workout.helper.infrastructure.services.WorkoutService;

import java.util.List;

@AllArgsConstructor
@Service
class UserWorkoutServiceImpl implements UserWorkoutService{
    private final WorkoutService workoutService;

    @Override
    public List<WorkoutStatisticsDto> getTrainingUnitWorkoutStatistics(String userId, String trainingUnitId) {
        return null;
    }
}
