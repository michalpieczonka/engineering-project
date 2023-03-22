package pm.workout.helper.domain.training.excercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pm.workout.helper.domain.training.excercise.TargetMuscle;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ExerciseDto {
    private final Long exerciseId;
    private final String exerciseName;
    private final String exerciseDescription;
    private final String videoUrl;
    private final TargetMuscle targetMuscle;
    private List<TargetMuscle> additionalMuscles;
}
