package pm.workout.helper.api.training.plan.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CreateTrainingUnitPartRequest {
    @NotNull
    private long exerciseId;
    @NotNull
    private List<UnitPartExerciseDetails> seriesRepetitionsDetails;
}
