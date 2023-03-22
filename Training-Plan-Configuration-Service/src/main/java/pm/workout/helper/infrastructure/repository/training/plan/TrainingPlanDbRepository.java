package pm.workout.helper.infrastructure.repository.training.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import pm.workout.helper.domain.training.plan.TrainingPlan;

import java.util.List;
import java.util.Optional;

interface TrainingPlanDbRepository extends JpaRepository<TrainingPlan, Long> {
    Optional<TrainingPlan> getTrainingPlanById(Long trainingPlanId);
    List<TrainingPlan> getTrainingPlansByPlanCreatorUserId(Long userId);
}
