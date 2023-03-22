package pm.workout.helper.domain.notification;

import pm.workout.helper.domain.notification.dto.NotificationDto;

public interface NotificationService {
    void processAndSaveNotification(NotificationDto notificationDto);
}
