package pm.workout.helper.domain.workout.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeriesRepetitionsDetails {
    private int seriesNumber;
    private int performedRepetitionsNumber;
    private int targetSeriesRepetitionsNumber;
    private double usedWeight;
}
