package pm.workout.helper.domain.workout;

import org.springframework.stereotype.Component;
import pm.workout.helper.domain.workout.doc.Workout;
import pm.workout.helper.domain.workout.dto.UserWorkoutDto;

@Component
public class UserWorkoutMapper {

        public UserWorkoutDto toDto(Workout userWorkout) {
            return new UserWorkoutDto(userWorkout.getWorkoutId(),
                    userWorkout.getTrainingPlanId(),
                    userWorkout.getTrainingUnitId(),
                    userWorkout.getStartedAt(),
                    userWorkout.getFinishedAt(),
                    userWorkout.getTrainingDay(),
                    userWorkout.getWorkoutParts(),
                    userWorkout.getWorkoutAssessment());
        }
}
