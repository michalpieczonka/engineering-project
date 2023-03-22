package pm.workout.helper.domain.workout.dto.basic_statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.workout.doc.TrainingDay;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolumeExerciseDto {
    private String workoutId;
    private LocalDateTime sessionFinishTime;
    private Long trainingUnitId;
    private TrainingDay trainingDay;
    private List<VolumeExerciseDetailsDto> volumeExerciseDetails;
}
