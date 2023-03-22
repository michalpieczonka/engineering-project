package pm.workout.helper.domain.training.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SeriesRepetitionsDto {
    private final int seriesNumber;
    private final int repetitionsNumber;
}
