package pm.workout.helper.domain.workout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatisticsDto {
    private int totalWorkouts;
    private int totalTrainingPlans;
    private long totalTrainingMinutes;
    private long averageTrainingTime;
    private double averageTrainingRate;
    private List<WorkoutsPerPlanDto> workoutsPerPlan;
}
