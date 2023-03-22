package pm.workout.helper.domain.notification;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pm.workout.helper.domain.notification.dto.NotificationDto;
import pm.workout.helper.domain.notification.email.EmailSenderService;

@AllArgsConstructor
@Service
@Log4j2
public class NotificationServiceImpl implements NotificationService{

    private final EmailSenderService emailSenderService;
    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public void processAndSaveNotification(NotificationDto notificationDto) {
       emailSenderService.sendEmail(notificationDto.getUserEmail(), mapNotificationTypeToSubject(notificationDto.getType()), notificationDto.getMessage());
       notificationRepository.save(buildNotification(notificationDto));
    }

    private String mapNotificationTypeToSubject(NotificationType type){
        return switch (type) {
            case WORKOUT_REMINDER -> "[Workout-helper] - dzisiaj trenujesz !";
            case PROFILE_UPDATE_REMINDER -> "[Workout-helper] - zaktualizuj profil !";
        };
    }

    private Notification buildNotification(NotificationDto notificationDto){
        return Notification.builder()
                .type(notificationDto.getType())
                .notifiedUserId(notificationDto.getUserId())
                .notifiedUserEmail(notificationDto.getUserEmail())
                .build();
    }
}
