package pm.workout.helper.domain.training.plan;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pm.workout.helper.domain.training.excercise.Exercise;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "training_unit_parts")
public class TrainingUnitPart {

    @Id
    @SequenceGenerator(
            name = "training_units",
            sequenceName = "training_units_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "training_units_sequence"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Exercise exercise;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private TrainingUnit trainingUnit;

    @Setter
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "training_unit_part_details",
            joinColumns = {@JoinColumn(name = "training_unit_part_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "number_of_series")
    @Column(name = "repetitions")
    private Map<Integer, Integer> seriesRepetitionsMap = new HashMap<>();

    public void addSeriesRepetitions(Integer series, Integer repetitions){
        seriesRepetitionsMap.put(series, repetitions);
    }

}
