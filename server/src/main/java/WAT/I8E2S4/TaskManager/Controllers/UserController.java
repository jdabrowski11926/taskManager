package WAT.I8E2S4.TaskManager.Controllers;

import WAT.I8E2S4.TaskManager.Model.Category;
import WAT.I8E2S4.TaskManager.Model.User;
import WAT.I8E2S4.TaskManager.Model.UserEditPassword;
import WAT.I8E2S4.TaskManager.Repositories.CategoryRepository;
import WAT.I8E2S4.TaskManager.Repositories.UserRepository;
import WAT.I8E2S4.TaskManager.Services.EmailService;
import WAT.I8E2S4.TaskManager.Services.UserService;
import WAT.I8E2S4.TaskManager.Model.Task;
import WAT.I8E2S4.TaskManager.Repositories.TaskRepository;
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
    private EmailService emailController;

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

}
