package pm.workout.helper.domain.training.plan.rate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "training_plans_rates")
public class TrainingPlanRate {
    @Id
    @SequenceGenerator(
            name = "training_plans",
            sequenceName = "training_plans_rates_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "training_plans_rates_sequence"
    )
    private Long id;

    private String description;

    private Integer rate;

    private Long rateAuthorUserId;

    private LocalDateTime rateDate;
}
