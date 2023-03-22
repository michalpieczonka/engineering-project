package pm.workout.helper.domain.training.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pm.workout.helper.domain.training.plan.PlanPriority;
import pm.workout.helper.domain.training.plan.TrainingPlanType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class TrainingPlanDto {
    private final long id;
    private final String title;
    private final String description;
    private final int numberOfTrainingDays;
    private final PlanPriority planPriority;
    private final boolean isPublic;
    private final TrainingPlanType planType;
    private final int preferredTrainingInternship;
    private final long planOwnerId;
    private final List<Long> planUsersIds;
    private LocalDateTime creationDate;
}
