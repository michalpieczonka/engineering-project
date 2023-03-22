package pm.workout.helper.infrastructure.repository.training.plan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.training.plan.TrainingPlan;
import pm.workout.helper.domain.training.plan.TrainingPlanRepository;
import pm.workout.helper.domain.training.plan.TrainingUnit;
import pm.workout.helper.domain.training.plan.TrainingUnitPart;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Repository
class TrainingPlanRepositoryImpl implements TrainingPlanRepository {
    private final TrainingPlanDbRepository trainingPlanDbRepository;
    private final TrainingUnitDbRepository trainingUnitDbRepository;
    private final EntityManager entityManager;

    @Override
    public List<TrainingPlan> getAllUserTrainingPlans(Long userId) {
        return trainingPlanDbRepository.getTrainingPlansByPlanCreatorUserId(userId);
    }

    @Override
    public Optional<TrainingPlan> getTrainingPlanDetails(Long trainingPlanId) {
        return trainingPlanDbRepository.getTrainingPlanById(trainingPlanId);
    }

    @Override
    public TrainingPlan createNewTrainingPlan(TrainingPlan trainingPlan) {
        return trainingPlanDbRepository.saveAndFlush(trainingPlan);
    }

    @Override
    public void deleteTrainingPlan(Long trainingPlanId) {

    }

    @Override
    public Optional<TrainingUnit> getTrainingUnit(Long trainingPlanId, TrainingDay trainingDay) {
        return trainingUnitDbRepository.getTrainingUnitByTrainingPlanIdAndTrainingDay(trainingPlanId, trainingDay);
    }

    @Override
    public Optional<TrainingPlan> getTrainingPlanById(Long trainingPlanId) {
        return trainingPlanDbRepository.getTrainingPlanById(trainingPlanId);
    }

    @Override
    public void updateTrainingPlan(TrainingPlan trainingPlan) {
        trainingPlanDbRepository.saveAndFlush(trainingPlan);
    }

    @Override
    public void updateTrainingUnit(TrainingUnit trainingUnit) {
        trainingUnitDbRepository.saveAndFlush(trainingUnit);
    }

    @Override
    public List<TrainingPlan> getAllPublicTrainingPlans() {
        return trainingPlanDbRepository.findAll();
    }

    @Override
    public void createNewTrainingPlanCopy(TrainingPlan plan, Set<TrainingUnit> baseUnits) {
        for (TrainingUnit unit : baseUnits) {
            TrainingUnit trainingUnit = unit.toBuilder()
                    .id(null)
                    .unitParts(new HashSet<>())
                    .trainingPlan(plan).build();
            entityManager.persist(trainingUnit);
            for (TrainingUnitPart part : new ArrayList<>(unit.getUnitParts())) {
                TrainingUnitPart trainingUnitPart = part.toBuilder().id(null).build();
                trainingUnitPart.setSeriesRepetitionsMap(new HashMap<>(part.getSeriesRepetitionsMap()));
                trainingUnitPart.setTrainingUnit(trainingUnit);
                entityManager.persist(trainingUnitPart);
            }
          plan.addTrainingUnit(trainingUnit);
        }
        trainingPlanDbRepository.save(plan);
    }
}
