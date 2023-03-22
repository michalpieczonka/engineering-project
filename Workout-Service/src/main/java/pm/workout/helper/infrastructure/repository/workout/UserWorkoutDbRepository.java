package pm.workout.helper.infrastructure.repository.workout;

import org.springframework.data.mongodb.repository.MongoRepository;
import pm.workout.helper.domain.workout.doc.UserWorkout;

public interface UserWorkoutDbRepository extends MongoRepository<UserWorkout, String> {

}
