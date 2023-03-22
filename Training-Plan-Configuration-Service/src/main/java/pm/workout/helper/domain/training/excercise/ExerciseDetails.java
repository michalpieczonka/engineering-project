package pm.workout.helper.domain.training.excercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDetails {
    private int repetitions;
    private int series;
}
