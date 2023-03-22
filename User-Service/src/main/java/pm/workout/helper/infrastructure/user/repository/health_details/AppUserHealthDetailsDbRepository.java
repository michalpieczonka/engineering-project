package pm.workout.helper.infrastructure.user.repository.health_details;

import org.springframework.data.jpa.repository.JpaRepository;
import pm.workout.helper.domain.user.health.AppUserHealthDetails;

public interface AppUserHealthDetailsDbRepository extends JpaRepository<AppUserHealthDetails, Long> {
}
