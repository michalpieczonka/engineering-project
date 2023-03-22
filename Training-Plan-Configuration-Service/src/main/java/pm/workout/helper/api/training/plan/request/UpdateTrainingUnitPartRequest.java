package pm.workout.helper.api.training.plan.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.training.plan.dto.SeriesRepetitionsDto;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainingUnitPartRequest {
    private Set<SeriesRepetitionsDto> seriesRepetitionsDetails;
}
