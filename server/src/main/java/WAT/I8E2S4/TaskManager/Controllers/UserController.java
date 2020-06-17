package WAT.I8E2S4.TaskManager.Controllers;

import WAT.I8E2S4.TaskManager.Category.Category;
import WAT.I8E2S4.TaskManager.Repositories.CategoryRepository;
import WAT.I8E2S4.TaskManager.Repositories.UserRepository;
import WAT.I8E2S4.TaskManager.Services.UserService;
import WAT.I8E2S4.TaskManager.Task.Task;
import WAT.I8E2S4.TaskManager.Repositories.TaskRepository;
import WAT.I8E2S4.TaskManager.User.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@AllArgsConstructor
@RestController
public class UserController {
    private CategoryRepository categoryRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailController emailController;

    private UserService userService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody User user){
        userService.signUp(user);
    }

    @PostMapping("/user/{username}/edit_account")
    @ResponseStatus(HttpStatus.OK)
    public void editAccount(@PathVariable String username, @RequestBody UserEditPassword editData){
        userService.editAccount(username, editData);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @Scheduled(fixedRate = 60000)
    public void searchForActiveTasks() {
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
                        this.emailController.sendEmail(users.get(i), tasks.get(k));
                    }
                }
            }
        }
    }
}
