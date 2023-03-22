package pm.workout.helper.domain.user.health;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pm.workout.helper.domain.user.AppUser;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "users_health_details")
@Setter
public class AppUserHealthDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGeneratorUsersHealthDetails")
    @SequenceGenerator(name = "sequenceGeneratorUser", sequenceName = "users_health_details_sequence", allocationSize = 1)
    private Long id;

    @OneToOne(cascade= CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser appUser;

    private double weight;
    private double height;
    private double waistCircuit; //pas
    private double waistCircumference; //talia
    private double armCircumference;
    private double thighCircumference;

    private LocalDateTime latestUpdatedTime;
}