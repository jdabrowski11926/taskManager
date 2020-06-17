package WAT.I8E2S4.TaskManager.Services;

import WAT.I8E2S4.TaskManager.Model.Category;
import WAT.I8E2S4.TaskManager.Model.Task;
import WAT.I8E2S4.TaskManager.Model.User;
import WAT.I8E2S4.TaskManager.Repositories.CategoryRepository;
import WAT.I8E2S4.TaskManager.Repositories.TaskRepository;
import WAT.I8E2S4.TaskManager.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static WAT.I8E2S4.TaskManager.Security.SecurityConstants.*;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSenderImpl javaMailSender;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private TaskRepository taskRepository;

    @Scheduled(fixedRate = 60000)
    public void searchForActiveTasks() {
        javaMailSender.setHost(MAIL_HOST);
        javaMailSender.setPort(MAIL_PORT);
        javaMailSender.setUsername(MAIL_USERNAME);
        javaMailSender.setPassword(MAIL_PASSWORD);
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
        currentTime = currentTime.replaceAll(" ","T");
        System.out.println("OBECNY CZAS : "+currentTime);
        List<User> users = userRepository.findAll();
        for(int i=0; i<users.size(); i++){
            List<Category> categories = categoryRepository.findAllByUserUsername(users.get(i).getUsername());
            for(int j=0; j<categories.size();j++){
                List<Task> tasks = taskRepository.findAllByCategory_NameAndCategory_User_username(categories.get(j).getName(),users.get(i).getUsername());
                for(int k=0; k<tasks.size(); k++){
                    if(tasks.get(k).getStartDateTime().toString().equals(currentTime) && tasks.get(k).isActive() && tasks.get(k).isNotification()){
                        sendEmail(users.get(i), tasks.get(k));
                    }
                }
            }
        }
    }

    public void sendEmail(User user, Task task) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("taskControllerTIM@gmail.com");
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Przypomnienie o zadaniu");
        mailMessage.setText("Na "+ task.getStartDateTime()+" - " + task.getEndDateTime() +
                " zostało zaplanowane zadanie: "+ task.getName() + "(opis : "+task.getDescription()+")");
        javaMailSender.send(mailMessage);
    }
}
