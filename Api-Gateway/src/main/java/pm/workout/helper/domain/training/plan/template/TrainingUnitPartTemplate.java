package pm.workout.helper.domain.training.plan.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TrainingUnitPartTemplate {
    private long unitPartId;
    private long exerciseId;
    private String exerciseName;
    private List<SeriesRepetitionsTemplate> targetSeriesRepetitionsDetails;
}
