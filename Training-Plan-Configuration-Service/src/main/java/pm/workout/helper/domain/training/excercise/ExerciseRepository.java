package pm.workout.helper.domain.training.excercise;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository {
     List<Exercise> getAllExercises();
     List<Exercise> getAllByMuscleGroup(TargetMuscle targetMuscle);
     void addExercise(Exercise exercise);
     Optional<Exercise> getExerciseById(Long id);
}
