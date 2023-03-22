package pm.workout.helper.domain.notification.email;

public interface EmailSenderService {
    void sendEmail(String targetEmail, String subject, String content);
}
