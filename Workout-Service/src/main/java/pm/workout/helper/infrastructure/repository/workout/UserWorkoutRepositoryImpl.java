package pm.workout.helper.infrastructure.repository.workout;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pm.workout.helper.domain.workout.UserWorkoutRepository;
import pm.workout.helper.domain.workout.doc.UserWorkout;
import pm.workout.helper.domain.workout.doc.Workout;

import java.util.ArrayList;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserWorkoutRepositoryImpl implements UserWorkoutRepository {

    private final UserWorkoutDbRepository userWorkoutMongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void saveUserWorkout(String userId, Workout workout) {
        UserWorkout userWorkout = userWorkoutMongoRepository.findById(userId).orElseGet(() -> new UserWorkout(userId, new ArrayList<>()));
        userWorkout.getPerformedWorkouts().add(workout);
        userWorkoutMongoRepository.save(userWorkout);
    }

    @Override
    public Optional<UserWorkout> getUserWorkouts(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, UserWorkout.class).stream().findFirst();
    }

    @Override
    public Optional<Workout> getWorkout(String workoutId) {
        Query query = new Query(Criteria.where("performedWorkouts.workoutId").is(workoutId));
        return mongoTemplate.findOne(query, UserWorkout.class).getPerformedWorkouts().stream().filter(workout -> workout.getWorkoutId().equals(workoutId)).findFirst();
    }

    @Override
    public void saveWorkout(UserWorkout userWorkout) {
        userWorkoutMongoRepository.save(userWorkout);
    }

    @Override
    public Optional<UserWorkout> findUserWorkoutsByWorkoutId(String workoutId) {
        Query query = new Query(Criteria.where("performedWorkouts.workoutId").is(workoutId));
        return Optional.ofNullable(mongoTemplate.findOne(query, UserWorkout.class));
    }
}
