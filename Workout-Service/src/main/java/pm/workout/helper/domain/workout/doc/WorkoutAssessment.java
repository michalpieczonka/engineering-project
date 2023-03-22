package pm.workout.helper.domain.workout.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutAssessment {
    private String additionalComment;
    private Integer personalRate;
}
