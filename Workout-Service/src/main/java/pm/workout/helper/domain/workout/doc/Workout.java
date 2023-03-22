package pm.workout.helper.domain.workout.doc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Workout {
    private String workoutId;
    private long trainingPlanId;
    private long trainingUnitId;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private TrainingDay trainingDay;
    private List<WorkoutPart> workoutParts;
    private WorkoutAssessment workoutAssessment;

    public long getWorkoutTime(){
        return Duration.between(startedAt, finishedAt).toMinutes();
    }
}
