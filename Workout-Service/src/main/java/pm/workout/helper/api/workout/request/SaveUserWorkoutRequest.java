package pm.workout.helper.api.workout.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.workout.doc.TrainingDay;
import pm.workout.helper.domain.workout.doc.WorkoutAssessment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaveUserWorkoutRequest {
    @NotBlank
    private String trainingPlanId;
    @NotBlank
    private String trainingUnitId;
    @NotNull
    private LocalDateTime startedAt;
    @NotNull
    private LocalDateTime finishedAt;
    @NotNull
    private TrainingDay trainingDay;
    @NotNull
    private List<SaveWorkoutPartsRequest> workoutParts;
    @NotNull
    private WorkoutAssessment workoutAssessment;
}
