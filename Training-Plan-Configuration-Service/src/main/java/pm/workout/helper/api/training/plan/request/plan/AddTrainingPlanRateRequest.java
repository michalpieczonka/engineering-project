package pm.workout.helper.api.training.plan.request.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddTrainingPlanRateRequest {
    private long rateAuthorId;
    private Integer rateValue;
    private String commentText;
}
