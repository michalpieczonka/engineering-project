package pm.workout.helper.domain.training.plan.rate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class TrainingPlanRateDto {
    private final long id;
    private final String description;
    private final Integer rate;
    private final LocalDateTime creationDate;
}
