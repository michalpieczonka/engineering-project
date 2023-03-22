package pm.workout.helper.api.workout.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.workout.doc.SeriesRepetitionsDetails;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaveWorkoutPartsRequest {
    @NotNull
    private long trainingUnitPartId;
    @NotNull
    private long exerciseId;
    @NotNull
    private String exerciseName;
    @NotNull
    private List<SaveSeriesRepetitionsRequest> seriesRepetitionsDetails;
}
