package pm.workout.helper.api.notification.queue;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pm.workout.helper.domain.notification.NotificationService;
import pm.workout.helper.domain.notification.dto.NotificationDto;

@Component
@AllArgsConstructor
@Log4j2
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.notification}")
    public void consumeNotification(NotificationDto notification){
        log.info("Received notification: {}", notification);
        notificationService.processAndSaveNotification(notification);
    }
}
