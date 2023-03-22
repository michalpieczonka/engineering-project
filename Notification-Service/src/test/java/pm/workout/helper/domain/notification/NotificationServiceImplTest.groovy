package pm.workout.helper.domain.notification


import pm.workout.helper.domain.notification.dto.NotificationDto
import pm.workout.helper.domain.notification.email.EmailSenderService
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicLong

class NotificationServiceImplTest extends Specification {
    def "when  trying to process and save notification, should process it, send email and save notification"() {
        given:
        def emailService = Mock(EmailSenderService)
        def notificationRepository = new NotificationRepositoryMock()
        def notificationService = new NotificationServiceImpl(emailService, notificationRepository)
        def notificationDto = new NotificationDto(NotificationType.WORKOUT_REMINDER, 1L, "tester@wp.pl", "test message")

        when:
        notificationService.processAndSaveNotification(notificationDto)

        then:
        1 * emailService.sendEmail('tester@wp.pl', '[Workout-helper] - dzisiaj trenujesz !', 'test message')
        notificationRepository.getAllNotifications().size() == 1
        notificationRepository.getAllNotifications().get(0).notifiedUserId == 1L
        notificationRepository.getAllNotifications().get(0).type == NotificationType.WORKOUT_REMINDER
        notificationRepository.getAllNotifications().get(0).notifiedUserEmail == "tester@wp.pl";
    }


    class NotificationRepositoryMock implements NotificationRepository {
        private final Map<String, Notification> NOTIFICATIONS_MAP = new HashMap<>();
        private AtomicLong seq = new AtomicLong(1L)

        List<Notification> getAllNotifications() {
            return NOTIFICATIONS_MAP.values().stream().toList()
        }

        NotificationRepositoryMock(){

        }

        @SuppressWarnings("GroovyAccessibility")
        @Override
        void save(Notification notification) {
            notification.id = seq.getAndIncrement()
            NOTIFICATIONS_MAP.put(notification.id.toString(), notification)
        }
    }
}
