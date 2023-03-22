package pm.workout.helper.domain.workout.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutPartDto {
    private long trainingUnitPartId;
    private long exerciseId;
    private String exerciseName;
    private List<SeriesRepetitionsDetails> seriesRepetitionsDetails;
}
