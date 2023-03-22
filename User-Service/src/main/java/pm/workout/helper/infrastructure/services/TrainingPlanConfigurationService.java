package pm.workout.helper.infrastructure.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.infrastructure.configuration.FeignConfiguration;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(value = "training-plan-configuration-service",
        path = "/training-plan/api",
        configuration = FeignConfiguration.class
)
public interface TrainingPlanConfigurationService {
    @GetMapping("/exercises/all")
    List<Object> getAllExercises();

    @GetMapping("/exercises")
    List<Object> getExercisesByTargetMuscle(@RequestParam("targetMuscle") @NotNull Object targetMuscle);

    @PostMapping("/exercises")
    void addNewExercise(@RequestBody @NotNull Object newExercise);

    @PostMapping("/training-plans/{userId}")
    void addNewUserTrainingPlan(@RequestBody @NotNull Object newTrainingPlan, @PathVariable Long userId) ;

    @GetMapping("/training-plans/{userId}/all")
    List<Object> getUserTrainingPlans(@PathVariable Long userId);

    @GetMapping("/training-plans/{trainingPlanId}")
    TrainingPlanDetailsDto getTrainingPlan(@PathVariable Long trainingPlanId);

    @PutMapping("/training-plans/{trainingPlanId}")
    void updateTrainingPlan(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId, @RequestBody @NotNull Object trainingPlan);

    @DeleteMapping("/training-plans/{trainingPlanId}/training-units/{trainingUnitId}/unit-parts/{trainingUnitPartId}")
    void deleteTrainingUnitPart(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                                @PathVariable("trainingUnitId") @NotNull Long trainingUnitId,
                                @PathVariable("trainingUnitPartId") @NotNull Long trainingUnitPartId);

    @PutMapping("/training-plans/{trainingPlanId}/training-units/{trainingUnitId}/unit-parts/{trainingUnitPartId}")
    void updateTrainingUnitPart(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                                @PathVariable("trainingUnitId") @NotNull Long trainingUnitId,
                                @PathVariable("trainingUnitPartId") @NotNull Long trainingUnitPartId,
                                @RequestBody @NotNull Object trainingUnitPart);

    @PostMapping("/training-plans/{trainingPlanId}/training-units/{trainingUnitId}")
    void addTrainingUnitPart(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                             @PathVariable("trainingUnitId") @NotNull Long trainingUnitId,
                             @RequestBody @NotNull Object trainingUnitPart);


    @PutMapping("/training-plans/{trainingPlanId}/copy/{userId}")
    void copyTrainingPlan(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                          @PathVariable("userId") @NotNull Long userId);

    @PostMapping("/training-plans/{trainingPlanId}/rate")
    void addPlanRate(@PathVariable("trainingPlanId") @NotNull Long trainingPlanId,
                     @RequestBody @NotNull Object rate);

}