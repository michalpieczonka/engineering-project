package pm.workout.helper.domain.workout.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.api.JsonLocalDateTimeSerializer;
import pm.workout.helper.domain.training.plan.TrainingDay;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserWorkoutDto {
    private String workoutId;
    private long trainingPlanId;
    private long trainingUnitId;
    @JsonLocalDateTimeSerializer
    private LocalDateTime startedAt;
    @JsonLocalDateTimeSerializer
    private LocalDateTime finishedAt;
    private TrainingDay trainingDay;
    private List<WorkoutPartDto> workoutParts;
    private WorkoutAssessmentDto workoutAssessment;

    public static UserWorkoutDto getEmptyDto(){
        return new UserWorkoutDto("-1", 0L, 0L,  null, null, null, Collections.emptyList(), null);
    }
}
