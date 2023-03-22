package pm.workout.helper.domain.workout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutsPerPlanDto {
    private long trainingPlanId;
    private int totalWorkouts;
}
