package pm.workout.helper.domain.training.plan.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pm.workout.helper.domain.training.plan.TargetMuscle;

import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
public class TrainingUnitPartDto {
    private final long id;
    private final long exerciseId; //mapping
    private final String exerciseName;
    private final TargetMuscle targetMuscle;
    private final Set<TargetMuscle> additionalMuscles;
    private final Set<SeriesRepetitionsDto> seriesRepetitionsDetails;
}
