package pm.workout.helper.domain.training.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SeriesRepetitionsDto {
    private int seriesNumber;
    private int repetitionsNumber;
}
