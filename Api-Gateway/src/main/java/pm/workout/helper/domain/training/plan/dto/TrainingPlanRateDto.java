package pm.workout.helper.domain.training.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pm.workout.helper.api.JsonLocalDateTimeSerializer;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class TrainingPlanRateDto {
    private final long id;
    private final String description;
    private final Integer rate;
    @JsonLocalDateTimeSerializer
    private LocalDateTime creationDate;
}
