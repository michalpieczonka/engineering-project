package pm.workout.helper.domain.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.user.health.AppUserHealthDetails;
import pm.workout.helper.domain.user.health.AppUserPhoto;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@EqualsAndHashCode
@Entity
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGeneratorUser")
    @SequenceGenerator(name = "sequenceGeneratorUser", sequenceName = "users_sequence", allocationSize = 1)
    private Long id;

    @Setter
    @ColumnTransformer(write = "lower(?)")
    private String username;

    @Setter
    private String password;

    @Setter
    private String email;

    @Setter
    private LocalDate dateOfBirth;

    private LocalDateTime registrationDateTime;

    @Setter
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Setter
    private Long currentTrainingPlanId;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "users_training_days", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "training_days")
    @Enumerated(EnumType.STRING)
    @Setter
    private Set<TrainingDay> trainingDays = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private UserAuthority userAuthority;

    @OneToOne(cascade= CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "health_details_id", referencedColumnName = "id")
    @Setter
    private AppUserHealthDetails healthDetails;

    @Builder.Default
    @Setter
    private boolean notificationsEnabled = false;

    public void updateHealthDetails(AppUserHealthDetails healthDetails) {
        this.healthDetails = healthDetails;
        healthDetails.setAppUser(this);
    }

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<AppUserPhoto> userPhotos = new HashSet<>();

    @Setter
    @Builder.Default
    private LocalDate trainingStartDate = LocalDate.now();


    public void addPhoto(AppUserPhoto photo) {
        userPhotos.add(photo);
    }

}
