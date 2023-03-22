package pm.workout.helper.infrastructure.repository.training.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.training.plan.TrainingUnit;

import java.util.Optional;

public interface TrainingUnitDbRepository extends JpaRepository<TrainingUnit, Long> {
    Optional<TrainingUnit> getTrainingUnitByTrainingPlanIdAndTrainingDay(Long trainingPlanId, TrainingDay trainingDay);
}
