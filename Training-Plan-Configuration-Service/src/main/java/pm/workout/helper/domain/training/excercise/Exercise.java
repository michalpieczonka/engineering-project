package pm.workout.helper.domain.training.excercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.training.plan.TrainingUnitPart;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @SequenceGenerator(
            name = "exercises",
            sequenceName = "exercises_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercises_sequence"
    )
    private Long id;

    private String name;

    private String description;

    private String videoUrl;

    @Enumerated(EnumType.STRING)
    private TargetMuscle targetMuscle;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @ElementCollection(targetClass = TargetMuscle.class)
    @JoinTable(name = "additional_muscles", joinColumns = @JoinColumn(name = "exercise_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "additional_muscle")
    private Set<TargetMuscle> additionalMuscles = new HashSet<>();

    @OneToMany(mappedBy = "exercise", fetch = FetchType.LAZY)
    private Set<TrainingUnitPart> trainingUnitParts = new HashSet<>();
}
