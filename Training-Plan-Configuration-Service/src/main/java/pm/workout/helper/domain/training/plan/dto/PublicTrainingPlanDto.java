package pm.workout.helper.domain.training.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.training.plan.PlanPriority;
import pm.workout.helper.domain.training.plan.TrainingPlanType;
import pm.workout.helper.domain.training.plan.rate.dto.TrainingPlanRateDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicTrainingPlanDto {
    private long id;
    private String title;
    private String description;
    private int numberOfTrainingDays;
    private PlanPriority planPriority;
    private boolean isPublic;
    private TrainingPlanType planType;
    private int preferredTrainingInternship;
    private long planOwnerId;
    private Set<Long> planUsersIds;
    private LocalDateTime creationDate;
    private Set<TrainingPlanRateDto> planRates;
    private double averageRate;
    private boolean isInUserPlans;
    private boolean isCreatedByRequestedUser;
}
