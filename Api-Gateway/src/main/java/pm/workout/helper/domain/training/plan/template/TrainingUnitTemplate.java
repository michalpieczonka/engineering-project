package pm.workout.helper.domain.training.plan.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pm.workout.helper.domain.training.plan.TrainingDay;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class TrainingUnitTemplate {
    private long trainingPlanId;
    private long trainingUnitId;
    private TrainingDay trainingDay;
    private List<TrainingUnitPartTemplate> trainingUnitParts;
}
