package pm.workout.helper.api.training.exercise.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import pm.workout.helper.domain.training.excercise.ExerciseType;
import pm.workout.helper.domain.training.excercise.TargetMuscle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateExerciseRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Nullable
    private String videoUrl;
    @NotNull
    private ExerciseType exerciseType;
    @NotNull
    private TargetMuscle targetMuscle;
    @Nullable
    private List<TargetMuscle> additionalMuscles;
}
