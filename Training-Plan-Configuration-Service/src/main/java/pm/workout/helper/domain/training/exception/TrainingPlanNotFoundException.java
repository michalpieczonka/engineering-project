package pm.workout.helper.domain.training.exception;

import pm.workout.helper.domain.exception.DomainError;
import pm.workout.helper.domain.exception.NotFoundException;

public class TrainingPlanNotFoundException extends NotFoundException {
    public TrainingPlanNotFoundException(Long trainingPlanId){
        super (String.format("Training plan %s not found", trainingPlanId), DomainError.TRAINING_PLAN_NOT_FOUND_EXCEPTION);
    }

}
