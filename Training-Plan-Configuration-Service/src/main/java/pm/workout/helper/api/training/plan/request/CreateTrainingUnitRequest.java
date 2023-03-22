package pm.workout.helper.api.training.plan.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.training.plan.TrainingDay;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateTrainingUnitRequest {
    @NotNull
    private TrainingDay trainingDay;
    @NotNull
    private List<CreateTrainingUnitPartRequest> trainingUnitParts;
}
