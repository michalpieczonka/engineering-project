package pm.workout.helper.domain.workout.dto;

import lombok.Data;

@Data
public class SeriesRepetitionsDetails {
    private int seriesNumber;
    private int performedRepetitionsNumber;
    private int targetSeriesRepetitionsNumber;
    private double usedWeight;
}
