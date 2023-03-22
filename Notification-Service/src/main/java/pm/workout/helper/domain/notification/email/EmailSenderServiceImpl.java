package pm.workout.helper.domain.notification.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${email.sender.name}")
    private String emailSenderName;

    @Override
    public void sendEmail(String targetEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSenderName);
        message.setTo(targetEmail);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
    }
}
