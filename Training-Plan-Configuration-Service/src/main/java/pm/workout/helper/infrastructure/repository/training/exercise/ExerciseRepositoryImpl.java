package pm.workout.helper.infrastructure.repository.training.exercise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pm.workout.helper.domain.training.excercise.Exercise;
import pm.workout.helper.domain.training.excercise.ExerciseRepository;
import pm.workout.helper.domain.training.excercise.ExerciseType;
import pm.workout.helper.domain.training.excercise.TargetMuscle;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Repository
class ExerciseRepositoryImpl implements ExerciseRepository {

    private final ExerciseDbRepository exerciseDbRepository;

    @Override
    public List<Exercise> getAllExercises() {
      //  return List.of(new Exercise(1L, "Wyciskanie na klatke", "Wyciskamy na klatke elegancko", "www.youtube.com/watch?serial=123", TargetMuscle.CHEST,ExerciseType.COMPOUNDED, Set.of(TargetMuscle.TRAPEZIUS, TargetMuscle.TRICEPS), Set.of()),
      //          new Exercise(2L, "Podciąganie", "Podciagamysie", "url wideo", TargetMuscle.BACK_DELTOIDS,ExerciseType.COMPOUNDED, Set.of(TargetMuscle.BICEPS), Set.of()));
        return exerciseDbRepository.findAll();
    }

    @Override
    public void addExercise(Exercise exercise) {
        exerciseDbRepository.save(exercise);
    }

    @Override
    public List<Exercise> getAllByMuscleGroup(TargetMuscle targetMuscle) {
        return exerciseDbRepository.findAllByTargetMuscle(targetMuscle);
      //  if (targetMuscle == TargetMuscle.BICEPS){
      //      return List.of( new Exercise(2L, "Podciąganie", "Podciagamysie", "url wideo", TargetMuscle.BICEPS, ExerciseType.COMPOUNDED, Set.of(TargetMuscle.BICEPS), Set.of()));
      //  } else {
      //      return List.of(new Exercise(1L, "Wyciskanie na klatke", "Wyciskamy na klatke elegancko", "www.youtube.com/watch?serial=123",
      //              TargetMuscle.CHEST, ExerciseType.COMPOUNDED, Set.of(TargetMuscle.TRAPEZIUS, TargetMuscle.TRICEPS), Set.of()));
      //  }
    }

    @Override
    public Optional<Exercise> getExerciseById(Long id) {
        return exerciseDbRepository.findById(id);
    }
}
