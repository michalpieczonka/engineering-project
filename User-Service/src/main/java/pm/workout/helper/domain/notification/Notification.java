package pm.workout.helper.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Notification {
    private NotificationType type;
    private Long userId;
    private String userEmail;
    private String message;
}
