package pm.workout.helper.domain.training.plan.exception;

import pm.workout.helper.domain.NotFoundException;
import pm.workout.helper.domain.exception.DomainError;

public class TrainingPlanNotFoundException extends NotFoundException {
    public TrainingPlanNotFoundException(long trainingPlanId) {
        super(String.format("Training plan with id %s not found", trainingPlanId), DomainError.TRAINING_PLAN_NOT_EXISTS);
    }

    public TrainingPlanNotFoundException(){
        super("Training plan with id %s not found in users plans", DomainError.USER_NOT_EXISTS_IN_PLAN_USERS);
    }
}
