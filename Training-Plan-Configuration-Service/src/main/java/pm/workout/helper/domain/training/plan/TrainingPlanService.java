package pm.workout.helper.domain.training.plan;

import pm.workout.helper.api.training.plan.request.CreateTrainingPlanRequest;
import pm.workout.helper.api.training.plan.request.CreateTrainingUnitPartRequest;
import pm.workout.helper.api.training.plan.request.UpdateTrainingPlanRequest;
import pm.workout.helper.api.training.plan.request.UpdateTrainingUnitPartRequest;
import pm.workout.helper.api.training.plan.request.plan.AddTrainingDayRequest;
import pm.workout.helper.api.training.plan.request.plan.AddTrainingPlanRateRequest;
import pm.workout.helper.domain.training.plan.dto.PublicTrainingPlanDto;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDto;
import pm.workout.helper.domain.training.plan.dto.template.TrainingUnitTemplateDto;

import java.util.List;

public interface TrainingPlanService {
    List<TrainingPlanDto> getAllUserTrainingPlans(Long userId);
    TrainingPlanDetailsDto getTrainingPlanDetails(Long trainingPlanId);
    Long createNewTrainingPlan(CreateTrainingPlanRequest request, Long userId);
    void deleteTrainingPlan(Long trainingPlanId);
    TrainingUnitTemplateDto getTrainingUnitPartTemplate(Long trainingPlanId, TrainingDay trainingDay);
    void updateTrainingPlan(Long trainingPlanId, UpdateTrainingPlanRequest updateTrainingPlanRequest);
    void deleteTrainingUnitPart(Long trainingPlanId, Long trainingUnitId, Long trainingUnitPartId);
    void addTrainingUnitPart(Long trainingPlanId, Long trainingUnitId, CreateTrainingUnitPartRequest request);
    void updateTrainingUnitPart(Long trainingPlanId, Long trainingUnitId, Long trainingUnitPartId, UpdateTrainingUnitPartRequest request);
    List<PublicTrainingPlanDto> getAllPublicTrainingPlans(Long requestedByUserId);
    void copyTrainingPlan(Long trainingPlanId, Long userId);
    void addTrainingPlanRate(Long trainingPlanId, AddTrainingPlanRateRequest request);
}
