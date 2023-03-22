package pm.workout.helper.infrastructure.queue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;
import pm.workout.helper.domain.notification.Notification;
import pm.workout.helper.domain.notification.NotificationRepository;
import pm.workout.helper.infrastructure.configuration.RabbitMQConfiguration;

@Component
@Slf4j
@AllArgsConstructor
public class NotificationProducer implements NotificationRepository{
    private final AmqpTemplate amqpTemplate;
    private final RabbitMQConfiguration notificationQueue;

    @Override
    public void sendNotification(Notification notification) {
       String exchange = notificationQueue.getInternalExchange();
       String routingKey = notificationQueue.getInternalNotificationRoutingKey();
            log.info("Sending update required notification: {} to {} using routing key {}",
                    notification,
                    exchange,
                    routingKey);
            amqpTemplate.convertAndSend(exchange, routingKey, notification);
    }
}
