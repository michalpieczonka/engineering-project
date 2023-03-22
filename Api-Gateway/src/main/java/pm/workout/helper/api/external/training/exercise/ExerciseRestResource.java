package pm.workout.helper.api.external.training.exercise;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pm.workout.helper.infrastructure.services.TrainingPlanConfigurationService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/exercises")
public class ExerciseRestResource {
    private final TrainingPlanConfigurationService trainingPlanConfigurationService;

    @GetMapping(path = "/all")
    @ResponseStatus(HttpStatus.OK)
    private List<Object> getAllExercises(){
        return trainingPlanConfigurationService.getAllExercises();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private List<Object> getExercisesByTargetMuscle(@RequestParam @NotNull Object targetMuscle){
        return trainingPlanConfigurationService.getExercisesByTargetMuscle(targetMuscle);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private void addNewExercise(@RequestBody @NotNull Object newExercise){
        trainingPlanConfigurationService.addNewExercise(newExercise);
    }
}
