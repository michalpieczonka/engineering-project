package pm.workout.helper.domain.training.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pm.workout.helper.api.JsonLocalDateTimeSerializer;
import pm.workout.helper.domain.training.plan.PlanPriority;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.training.plan.TrainingPlanType;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Builder
@Getter
public class TrainingPlanDetailsDto {
    private final long id;
    private final String title;
    private final String planDescription;
    private final int numberOfTrainingDays;
    private final Set<TrainingDay> trainingDays;
    private final PlanPriority planPriority;
    private final TrainingPlanType planType;
    private final int preferredTrainingInternship;
    private final long planCreatorUserId;
    private final boolean isPublic;
    private final Set<TrainingPlanRateDto> planRates;
    private final Set<TrainingUnitDto> trainingUnits;
    private final Set<Long> planUsersIds;
    @JsonLocalDateTimeSerializer
    private final LocalDateTime creationDate;
}