package pm.workout.helper.api.training.exercise;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pm.workout.helper.api.training.exercise.request.CreateExerciseRequest;
import pm.workout.helper.domain.training.excercise.ExerciseService;
import pm.workout.helper.domain.training.excercise.TargetMuscle;
import pm.workout.helper.domain.training.excercise.dto.ExerciseDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/training-plan/api/exercises")
public class ExerciseRestResource {
    private final ExerciseService exerciseService;

    @GetMapping("/all")
    private List<ExerciseDto> getAllExercises(){
        return exerciseService.getAllExercises();
    }

    @GetMapping
    private List<ExerciseDto> getExercisesByTargetMuscles(@RequestParam("targetMuscle") @NotNull TargetMuscle targetMuscle){
        return exerciseService.getExercisesByTargetMuscleGroup(targetMuscle);
    }

    @PostMapping
    private void addNewExercise(@RequestBody @NotNull @Valid CreateExerciseRequest createExerciseRequest){
        exerciseService.addExercise(createExerciseRequest);
    }
}
