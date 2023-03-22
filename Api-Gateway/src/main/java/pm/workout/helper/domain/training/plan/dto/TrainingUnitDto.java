package pm.workout.helper.domain.training.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pm.workout.helper.domain.training.plan.TrainingDay;

import java.util.Set;

@AllArgsConstructor
@Builder
@Getter
public class TrainingUnitDto {
    private final long id;
    private final TrainingDay trainingDay;
    private final Set<TrainingUnitPartDto> trainingUnitParts;
}