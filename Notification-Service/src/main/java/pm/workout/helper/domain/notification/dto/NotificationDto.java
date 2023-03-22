package pm.workout.helper.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.notification.NotificationType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotificationDto {
    private NotificationType type;
    private Long userId;
    private String userEmail;
    private String message;
}
