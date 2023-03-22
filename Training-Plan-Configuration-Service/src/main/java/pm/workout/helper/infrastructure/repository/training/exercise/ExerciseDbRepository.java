package pm.workout.helper.infrastructure.repository.training.exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import pm.workout.helper.domain.training.excercise.Exercise;
import pm.workout.helper.domain.training.excercise.TargetMuscle;

import java.util.List;

interface ExerciseDbRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByTargetMuscle(TargetMuscle targetMuscle);
}
