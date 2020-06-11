package WAT.I8E2S4.TaskManager.User;

import WAT.I8E2S4.TaskManager.Category.Category;
import WAT.I8E2S4.TaskManager.Category.CategoryRepository;
import WAT.I8E2S4.TaskManager.Task.Task;
import WAT.I8E2S4.TaskManager.Task.TaskRepository;
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

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody User user){
        if(user.getPassword()=="" || user.getUsername()==""){
            throw new UserNoDataException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.findByUsername(user.getUsername()).ifPresent(
                u -> {
                    throw new UserAlreadyExistsException();
                }
        );
        userRepository.save(user);
    }

    @PostMapping("/user/{username}/edit_account")
    @ResponseStatus(HttpStatus.OK)
    public void editAccount(@PathVariable String username, @RequestBody UserEditPassword editData){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
        String oldPassword = editData.getOldPassword();
        String newPassword = editData.getNewPassword();
        if(newPassword=="") throw new UserNoDataException("Password can not be empty");
        if(!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
            throw new InvalidOldPasswordException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> showUsers(){
        return userRepository.findAll();
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
