package pm.workout.helper.api.external.training;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pm.workout.helper.domain.training.plan.dto.PublicTrainingPlanDto;
import pm.workout.helper.infrastructure.services.TrainingPlanConfigurationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/training-plans")
public class TrainingPlanRestResource {
    private final TrainingPlanConfigurationService trainingPlanConfigurationService;

    @PostMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    void addNewTrainingPlan(@RequestBody @NotNull @Valid Object request, @PathVariable("userId") long userId) {
        trainingPlanConfigurationService.addNewUserTrainingPlan(request, userId);
    }

    @GetMapping(path = "/{userId}/all")
    @ResponseStatus(HttpStatus.OK)
    List<Object> getTrainingPlans(@PathVariable("userId") long userId) {
        return trainingPlanConfigurationService.getUserTrainingPlans(userId);
    }

    @GetMapping(path = "/{trainingPlanId}")
    @ResponseStatus(HttpStatus.OK)
    Object getTrainingPlan(@PathVariable("trainingPlanId") long trainingPlanId) {
        return trainingPlanConfigurationService.getTrainingPlan(trainingPlanId);
    }


    @PutMapping("/{trainingPlanId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateTrainingPlan(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId, @RequestBody @NotNull Object trainingPlan) {
        System.out.println(trainingPlan.toString());
        trainingPlanConfigurationService.updateTrainingPlan(trainingPlanId, trainingPlan);
    }

    @DeleteMapping("/{trainingPlanId}/training-units/{trainingUnitId}/unit-parts/{trainingUnitPartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTrainingUnitPart(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                                @PathVariable("trainingUnitId") @NotNull Long trainingUnitId,
                                @PathVariable("trainingUnitPartId") @NotNull Long trainingUnitPartId) {
        trainingPlanConfigurationService.deleteTrainingUnitPart(trainingPlanId, trainingUnitId, trainingUnitPartId);
    }

    @PutMapping("/{trainingPlanId}/training-units/{trainingUnitId}/unit-parts/{trainingUnitPartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateTrainingUnitPart(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                                @PathVariable("trainingUnitId") @NotNull Long trainingUnitId,
                                @PathVariable("trainingUnitPartId") @NotNull Long trainingUnitPartId,
                                @RequestBody @NotNull Object trainingUnitPart) {
        trainingPlanConfigurationService.updateTrainingUnitPart(trainingPlanId, trainingUnitId, trainingUnitPartId, trainingUnitPart);
    }

    @PostMapping("/{trainingPlanId}/training-units/{trainingUnitId}")
    @ResponseStatus(HttpStatus.CREATED)
    void addTrainingUnitPart(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                             @PathVariable("trainingUnitId") @NotNull Long trainingUnitId,
                             @NotNull @Valid @RequestBody Object request) {
        trainingPlanConfigurationService.addTrainingUnitPart(trainingPlanId, trainingUnitId, request);
    }

    @GetMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    List<PublicTrainingPlanDto> getPublicTrainingPlans(@RequestParam("requestedByUserId") @NotNull Long requestedByUserId) {
        return trainingPlanConfigurationService.getAllPublicTrainingPlans(requestedByUserId);
    }

    @PostMapping("/{trainingPlanId}/copy/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    void copyTrainingPlan(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                          @PathVariable("userId") @NotNull Long userId) {
        trainingPlanConfigurationService.copyTrainingPlan(trainingPlanId, userId);
    }

    @PostMapping("/{trainingPlanId}/rates")
    @ResponseStatus(HttpStatus.CREATED)
    void addTrainingPlanRate(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                             @RequestBody @NotNull Object request) {
        trainingPlanConfigurationService.addPlanRate(trainingPlanId, request);
    }

}
