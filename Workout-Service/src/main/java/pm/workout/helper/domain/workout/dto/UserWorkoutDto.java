package pm.workout.helper.domain.workout.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.api.workout.JsonLocalDateTimeSerializer;
import pm.workout.helper.domain.workout.doc.TrainingDay;
import pm.workout.helper.domain.workout.doc.UserWorkout;
import pm.workout.helper.domain.workout.doc.WorkoutAssessment;
import pm.workout.helper.domain.workout.doc.WorkoutPart;

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
    private List<WorkoutPart> workoutParts;
    private WorkoutAssessment workoutAssessment;

    public static UserWorkoutDto createEmptyDto(){
        return new UserWorkoutDto(null, 0L, 0L,  null, null, null, Collections.emptyList(), null);
    }
}
