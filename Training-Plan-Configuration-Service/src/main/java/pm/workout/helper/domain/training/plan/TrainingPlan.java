package pm.workout.helper.domain.training.plan;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pm.workout.helper.domain.training.plan.rate.TrainingPlanRate;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "training_plans")
public class TrainingPlan {

    @Id
    @SequenceGenerator(
            name = "training_plans",
            sequenceName = "training_plans_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "training_plans_sequence"
    )
    private Long id;

    private String title;

    private String description;

    private Integer numberOfTrainingDays;

    @Enumerated(EnumType.STRING)
    private PlanPriority planPriority;

    @Enumerated(EnumType.STRING)
    private TrainingPlanType type;

    private boolean isPublic;

    private int planLength;

    private int preferredTrainingInternship;

    private Long planCreatorUserId;

    private LocalDateTime creationDate;

    @Setter
    private LocalDateTime latestModificationDate;

    @Setter
    private LocalDate targetFinishDate;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "plan_users", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "plan_users")
    private Set<Long> planUsersIds = new HashSet<>();

    @Builder.Default
    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TrainingPlanRate> planRates = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "trainingPlan", cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<TrainingUnit> trainingUnits = new HashSet<>();

    public void setPlanOwner(Long planCreatorUserId){
        this.planCreatorUserId = planCreatorUserId;
        this.planUsersIds.add(planCreatorUserId);
    }

    public void addTrainingUnit(TrainingUnit trainingUnit){
        this.trainingUnits.add(trainingUnit);
    }

    public void addPlanUser(Long userId){
        this.planUsersIds.add(userId);
    }

    public void addPlanRate(TrainingPlanRate rate){
        this.planRates.add(rate);
    }

    public double getPlanRate(){
        double sum = planRates.stream().map(TrainingPlanRate::getRate)
                .reduce(0, Integer::sum)
                .doubleValue();
        return sum/planRates.size();
    }
}
