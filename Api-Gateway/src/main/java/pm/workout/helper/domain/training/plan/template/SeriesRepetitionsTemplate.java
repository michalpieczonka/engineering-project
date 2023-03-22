package pm.workout.helper.domain.training.plan.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeriesRepetitionsTemplate {
    private int seriesNumber;
    private int targetRepetitionsNumber;
    private int pastWorkoutRepetitionsNumber;
    private double pastWorkoutUsedWeight;
}
