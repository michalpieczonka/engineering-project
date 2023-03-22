package pm.workout.helper.domain.training.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pm.workout.helper.api.JsonLocalDateTimeSerializer;
import pm.workout.helper.domain.training.plan.PlanPriority;
import pm.workout.helper.domain.training.plan.TrainingPlanType;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<Long> planUsersIds;
    @JsonLocalDateTimeSerializer
    private LocalDateTime creationDate;
    private List<TrainingPlanRateDto> planRates;
    private double averageRate;
    private boolean isInUserPlans;
    private boolean isCreatedByRequestedUser;
}
