package pm.workout.helper.domain.training.excercise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pm.workout.helper.api.training.exercise.request.CreateExerciseRequest;
import pm.workout.helper.domain.training.excercise.dto.ExerciseDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class ExerciseServiceImpl implements ExerciseService{
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ExerciseDto> getAllExercises() {
        return exerciseRepository.getAllExercises()
                .stream().map(exerciseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void addExercise(CreateExerciseRequest exerciseRequest) {
        exerciseRepository.addExercise(createExercise(exerciseRequest));
    }

    @Override
    public List<ExerciseDto> getExercisesByTargetMuscleGroup(TargetMuscle targetMuscle) {
        return exerciseRepository.getAllByMuscleGroup(targetMuscle)
                .stream().map(exerciseMapper::toDto)
                .collect(Collectors.toList());
    }

    private Exercise createExercise(CreateExerciseRequest exerciseRequest){
        var builder = Exercise.builder()
                .name(exerciseRequest.getName())
                .description(exerciseRequest.getDescription())
                .targetMuscle(exerciseRequest.getTargetMuscle())
                .exerciseType(exerciseRequest.getExerciseType());
        Optional.ofNullable(exerciseRequest.getVideoUrl()).ifPresent(builder::videoUrl);
        Optional.ofNullable(exerciseRequest.getAdditionalMuscles()).ifPresent(e -> builder.additionalMuscles(new HashSet<>(e)));
        return builder.build();
    }
}
