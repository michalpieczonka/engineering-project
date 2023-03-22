package pm.workout.helper.api.training.plan.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.training.plan.PlanPriority;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.training.plan.TrainingPlanType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateTrainingPlanRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private List<TrainingDay> trainingDays;
    @NotNull
    private PlanPriority planPriority;
    @NotNull
    private TrainingPlanType planType;
    @NotNull
    private boolean isPublic;
    @NotNull
    private LocalDate targetFinishDate;
    @NotNull
    private List<CreateTrainingUnitRequest> trainingUnitsDetails;

}
