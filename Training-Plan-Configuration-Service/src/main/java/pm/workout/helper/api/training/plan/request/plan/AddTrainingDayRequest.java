package pm.workout.helper.api.training.plan.request.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.api.training.plan.request.CreateTrainingUnitPartRequest;
import pm.workout.helper.domain.training.plan.TrainingDay;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddTrainingDayRequest {
    @NotNull
    private TrainingDay trainingDay;
    @NotNull
    private List<CreateTrainingUnitPartRequest> trainingUnitParts;
}
