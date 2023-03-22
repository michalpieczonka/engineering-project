package pm.workout.helper.infrastructure.user;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pm.workout.helper.domain.user.UserService;

@Configuration
@EnableScheduling
@AllArgsConstructor
@Log4j2
public class UserNotificationScheduler {
    private final UserService userService;

    @Scheduled(cron="0 00 9 * * ? ", zone="Europe/Warsaw")
    public void dailyUsersUpdateRequiredNotificationTask() {
        log.info("Starting daily users update required notification task");
        userService.findUsersWithUpdateRequiredAndSendNotification();
        log.info("Daily users update required notification task finished");
    }


    @Scheduled(cron="0 00 10 * * ? ", zone="Europe/Warsaw")
    public void dailyUsersTrainingReminderNotificationTask() {
        log.info("Starting daily training reminder notification task");
        userService.findUsersWithTrainingTodayAndSendReminderNotification();
        log.info("Daily training reminder task finished");
    }
}
