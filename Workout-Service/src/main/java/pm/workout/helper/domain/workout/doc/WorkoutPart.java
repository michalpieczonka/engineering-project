package pm.workout.helper.domain.workout.doc;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPart {
    private long trainingUnitPartId;
    private long exerciseId;
    private String exerciseName;
    private List<SeriesRepetitionsDetails> seriesRepetitionsDetails;

    public double calculateVolume(){
        AtomicDouble volume = new AtomicDouble(0);
        seriesRepetitionsDetails.forEach(details -> {
            volume.getAndAdd(details.getPerformedRepetitionsNumber() * details.getUsedWeight());
        });
        return volume.get();
    }

    //1RM = weight lifted / (1.0278 - (0.0278 x reps))
    public double calculateOneRepMax(){
        AtomicDouble oneRepMax = new AtomicDouble(0);
        seriesRepetitionsDetails.forEach(details -> {
            oneRepMax.getAndAdd((double) Math.round((details.getUsedWeight() / (1.0278 - (0.0278 * details.getPerformedRepetitionsNumber())) * 1000) / 1000));
        });
        return oneRepMax.get();
    }

    public int calculateTotalRepetitions(){
        AtomicInteger totalRepetitions = new AtomicInteger(0);
        seriesRepetitionsDetails.forEach(details -> {
            totalRepetitions.getAndAdd(details.getPerformedRepetitionsNumber());
        });
        return totalRepetitions.intValue();
    }

    public int calculateTotalSeries(){
        return seriesRepetitionsDetails.size();
    }
}