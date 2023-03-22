package pm.workout.helper.domain.training.plan;

import pm.workout.helper.domain.training.plan.template.TrainingUnitTemplate;

public interface TrainingPlanService {
    TrainingUnitTemplate getTrainingUnitTemplate(long userId, long trainingPlanId, long trainingUnitId);
}
