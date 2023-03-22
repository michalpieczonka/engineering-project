package pm.workout.helper.infrastructure.repository.notification;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pm.workout.helper.domain.notification.Notification;
import pm.workout.helper.domain.notification.NotificationRepository;

@AllArgsConstructor
@Repository
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationDbRepository notificationDbRepository;

    @Override
    public void save(Notification notification) {
        notificationDbRepository.save(notification);
    }
}
