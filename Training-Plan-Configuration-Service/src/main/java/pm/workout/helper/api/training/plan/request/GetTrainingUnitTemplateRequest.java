package pm.workout.helper.api.training.plan.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.training.plan.TrainingDay;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTrainingUnitTemplateRequest {
    @NotNull
    private TrainingDay trainingDay;
}
