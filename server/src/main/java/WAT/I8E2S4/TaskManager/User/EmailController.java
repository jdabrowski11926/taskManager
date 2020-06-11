package WAT.I8E2S4.TaskManager.User;

import WAT.I8E2S4.TaskManager.Task.Task;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.RestController;

import static WAT.I8E2S4.TaskManager.security.SecurityConstants.*;

@RestController
public class EmailController {

    private JavaMailSenderImpl javaMailSender;

    EmailController(){
        javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(MAIL_HOST);
        javaMailSender.setPort(MAIL_PORT);
        javaMailSender.setUsername(MAIL_USERNAME);
        javaMailSender.setPassword(MAIL_PASSWORD);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("taskControllerTIM@gmail.com");
        mailMessage.setTo("testmail@gmail.com");
        mailMessage.setSubject("Przypomnienie o zadaniu");
        mailMessage.setText("test");
        javaMailSender.send(mailMessage);
    }

    void sendEmail(User user, Task task) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("taskControllerTIM@gmail.com");
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Przypomnienie o zadaniu");
        mailMessage.setText("Na "+ task.getStartDateTime()+" - " + task.getEndDateTime() +
                " zostało zaplanowane zadanie: "+ task.getName() + "(opis : "+task.getDescription()+")");
        javaMailSender.send(mailMessage);
    }
}
