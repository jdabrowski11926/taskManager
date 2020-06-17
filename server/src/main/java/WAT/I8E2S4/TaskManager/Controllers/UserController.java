package WAT.I8E2S4.TaskManager.Controllers;

import WAT.I8E2S4.TaskManager.Model.User;
import WAT.I8E2S4.TaskManager.Model.UserEditPassword;
import WAT.I8E2S4.TaskManager.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
public class UserController {

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
