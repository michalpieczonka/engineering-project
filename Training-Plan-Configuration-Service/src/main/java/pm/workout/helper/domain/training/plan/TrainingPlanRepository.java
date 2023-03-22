package pm.workout.helper.domain.training.plan;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainingPlanRepository {
    List<TrainingPlan> getAllUserTrainingPlans(Long userId);
    Optional<TrainingPlan> getTrainingPlanDetails(Long trainingPlanId);
    TrainingPlan createNewTrainingPlan(TrainingPlan trainingPlan);
    void deleteTrainingPlan(Long trainingPlanId);
    Optional<TrainingUnit> getTrainingUnit(Long trainingPlanId, TrainingDay trainingDay);
    Optional<TrainingPlan> getTrainingPlanById(Long trainingPlanId);
    void updateTrainingPlan(TrainingPlan trainingPlan);
    void updateTrainingUnit(TrainingUnit trainingUnit);
    List<TrainingPlan> getAllPublicTrainingPlans();
    void createNewTrainingPlanCopy(TrainingPlan plan, Set<TrainingUnit> baseUnits);
}
