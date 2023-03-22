package pm.workout.helper.domain.training.plan.dto.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.training.plan.dto.TrainingUnitPartDto;

import java.util.Set;

@AllArgsConstructor
@Getter
public class TrainingUnitTemplateDto {
    private final long trainingPlanId;
    private final long trainingUnitId;
    private final TrainingDay trainingDay;
    private final Set<TrainingUnitPartTemplateDto> trainingUnitParts;
}
