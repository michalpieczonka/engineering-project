package pm.workout.helper.domain.workout;

import pm.workout.helper.domain.workout.doc.UserWorkout;
import pm.workout.helper.domain.workout.doc.Workout;

import java.util.Optional;

public interface UserWorkoutRepository {
    void saveUserWorkout(String userId, Workout workout);
    Optional<UserWorkout> getUserWorkouts(String userId);
    Optional<Workout> getWorkout(String workoutId);
    void saveWorkout(UserWorkout userWorkout);
    Optional<UserWorkout> findUserWorkoutsByWorkoutId(String workoutId);
}
