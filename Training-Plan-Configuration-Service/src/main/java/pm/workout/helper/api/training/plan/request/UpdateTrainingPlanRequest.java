package pm.workout.helper.api.training.plan.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.training.plan.PlanPriority;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.training.plan.TrainingPlanType;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTrainingPlanRequest {
    private String title;
    private String planDescription;
    private Set<TrainingDay> trainingDays;
    private PlanPriority planPriority;
    private TrainingPlanType planType;
    private boolean isPublic;
}
