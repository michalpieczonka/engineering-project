package pm.workout.helper.api.workout.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaveSeriesRepetitionsRequest {
    @NotNull
    @Positive
    private int seriesNumber;
    @NotNull
    @Positive
    private int performedRepetitionsNumber;
    @NotNull
    @Positive
    private int targetSeriesRepetitionsNumber;
    @NotNull
    @Positive
    private double usedWeight;
}
