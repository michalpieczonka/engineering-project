package pm.workout.helper.api.training.plan;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pm.workout.helper.api.training.plan.request.CreateTrainingPlanRequest;
import pm.workout.helper.api.training.plan.request.CreateTrainingUnitPartRequest;
import pm.workout.helper.api.training.plan.request.UpdateTrainingPlanRequest;
import pm.workout.helper.api.training.plan.request.UpdateTrainingUnitPartRequest;
import pm.workout.helper.api.training.plan.request.plan.AddTrainingPlanRateRequest;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.training.plan.TrainingPlanService;
import pm.workout.helper.domain.training.plan.dto.PublicTrainingPlanDto;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDto;
import pm.workout.helper.domain.training.plan.dto.template.TrainingUnitTemplateDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/training-plan/api/training-plans")
public class TrainingPlanRestResource {
    private final TrainingPlanService trainingPlanService;

    @PostMapping(path = "/{userId}")
    private void createNewTrainingPlan(@PathVariable("userId") @NotNull Long userId,
                                            @NotNull @Valid @RequestBody CreateTrainingPlanRequest createTrainingPlanRequest){
        trainingPlanService.createNewTrainingPlan(createTrainingPlanRequest, userId);
    }

    @GetMapping(path = "/{trainingPlanId}")
    private TrainingPlanDetailsDto getTrainingPlan(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId) {
        return trainingPlanService.getTrainingPlanDetails(trainingPlanId);
    }

    @PutMapping(path = "/{trainingPlanId}")
    private void updateTrainingPlan(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId, @NotNull @Valid @RequestBody UpdateTrainingPlanRequest updateTrainingPlanRequest){
        trainingPlanService.updateTrainingPlan(trainingPlanId, updateTrainingPlanRequest);
    }

    @DeleteMapping(path = "/{trainingPlanId}/training-units/{trainingUnitId}/unit-parts/{trainingUnitPartId}")
    private void deleteTrainingUnitPart(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                                       @PathVariable("trainingUnitId") @NotNull Long trainingUnitId,
                                       @PathVariable("trainingUnitPartId") @NotNull Long trainingUnitPartId){
        trainingPlanService.deleteTrainingUnitPart(trainingPlanId, trainingUnitId, trainingUnitPartId);
    }

    @PostMapping(path = "/{trainingPlanId}/training-units/{trainingUnitId}")
    private void addTrainingUnitPart(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                                     @PathVariable("trainingUnitId") @NotNull Long trainingUnitId,
                                     @NotNull @Valid @RequestBody CreateTrainingUnitPartRequest request){
        trainingPlanService.addTrainingUnitPart(trainingPlanId, trainingUnitId, request);
    }

    @PutMapping(path = "/{trainingPlanId}/training-units/{trainingUnitId}/unit-parts/{trainingUnitPartId}")
    private void updateTrainingUnitPart(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                                     @PathVariable("trainingUnitId") @NotNull Long trainingUnitId,
                                     @PathVariable("trainingUnitPartId") @NotNull Long trainingUnitPartId,
                                     @NotNull @Valid @RequestBody UpdateTrainingUnitPartRequest request){
        trainingPlanService.updateTrainingUnitPart(trainingPlanId, trainingUnitId, trainingUnitPartId, request);
    }


    @GetMapping(path = "/{userId}/all")
    private List<TrainingPlanDto> getAllUserTrainingPlans(@PathVariable("userId") @NotNull Long userId){
        return trainingPlanService.getAllUserTrainingPlans(userId);
    }

    @GetMapping(path = "/{trainingPlanId}/template")
    private TrainingUnitTemplateDto getTrainingUnitPartTemplate(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                                                                @RequestParam("targetMuscle") @NotNull TrainingDay trainingDay){
        return trainingPlanService.getTrainingUnitPartTemplate(trainingPlanId, trainingDay);
    }

    @GetMapping(path = "/public")
    private List<PublicTrainingPlanDto> getAllPublicTrainingPlans (@RequestParam("requestedByUserId") @NotNull Long userId){
        return trainingPlanService.getAllPublicTrainingPlans(userId);
    }

    @PutMapping(path = "{trainingPlanId}/copy/{userId}")
    private void copyTrainingPlan(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                                  @PathVariable("userId") @NotNull Long userId){
        trainingPlanService.copyTrainingPlan(trainingPlanId, userId);
    }

    @PostMapping(path = "/{trainingPlanId}/rate")
    private void addTrainingPlanRate(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                                     @RequestBody AddTrainingPlanRateRequest request){
        trainingPlanService.addTrainingPlanRate(trainingPlanId, request);
    }
}
