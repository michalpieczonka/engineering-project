package pm.workout.helper.domain.training.plan;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "training_units")
public class TrainingUnit {
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

    @Enumerated(EnumType.STRING)
    private TrainingDay trainingDay;

    @Builder.Default
    @OneToMany (mappedBy = "trainingUnit", cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<TrainingUnitPart> unitParts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private TrainingPlan trainingPlan;


    public void addTrainingPart(TrainingUnitPart trainingUnitPart){
        this.unitParts.add(trainingUnitPart);
        trainingUnitPart.setTrainingUnit(this);
    }
}
