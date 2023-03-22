package pm.workout.helper.domain.training.excercise;

import pm.workout.helper.api.training.exercise.request.CreateExerciseRequest;
import pm.workout.helper.domain.training.excercise.dto.ExerciseDto;

import java.util.List;

public interface ExerciseService {
    List<ExerciseDto> getAllExercises();
    List<ExerciseDto> getExercisesByTargetMuscleGroup(TargetMuscle targetMuscle);
    void addExercise(CreateExerciseRequest exerciseRequest);
}
