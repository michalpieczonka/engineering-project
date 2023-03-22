package pm.workout.helper.domain.training.excercise.exception;

import pm.workout.helper.domain.shared.NotFoundException;

public class ExerciseNotFoundException extends NotFoundException {
    public ExerciseNotFoundException(Long id) {
        super(String.format("Exercise with id  %s not found", id));
    }
}
