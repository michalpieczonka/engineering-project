package pm.workout.helper.infrastructure.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import pm.workout.helper.domain.notification.Notification;

public interface NotificationDbRepository extends JpaRepository<Notification, Long> {

}
