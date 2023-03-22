package pm.workout.helper.domain.workout.dto.basic_statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//Eply formula
//1RM = weight lifted / (1.0278 - (0.0278 x reps))
public class VolumeExerciseDetailsDto {
    private Long exerciseId;
    private String exerciseName;
    private double totalVolume;
    private double oneRepMax;
}

