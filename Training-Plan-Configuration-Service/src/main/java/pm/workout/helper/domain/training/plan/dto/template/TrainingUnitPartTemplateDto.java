package pm.workout.helper.domain.training.plan.dto.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pm.workout.helper.domain.training.excercise.TargetMuscle;
import pm.workout.helper.domain.training.plan.dto.SeriesRepetitionsDto;

import java.util.Set;

@AllArgsConstructor
@Getter
public class TrainingUnitPartTemplateDto {
    private final long exerciseId;
    private final String exerciseName;
    private final String exerciseDescription;
    private final String videoUrl;
    private final TargetMuscle targetMuscle;
    private final Set<SeriesRepetitionsDto> seriesRepetitionsDetails;
}
