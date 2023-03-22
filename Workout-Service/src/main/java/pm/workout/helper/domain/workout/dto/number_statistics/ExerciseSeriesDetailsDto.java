package pm.workout.helper.domain.workout.dto.number_statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseSeriesDetailsDto {
    long exerciseId;
    String exerciseName;
    int totalSeries;
    int totalReps;
}
