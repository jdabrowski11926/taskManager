package WAT.I8E2S4.TaskManager.User;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
public class UserController {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody User user){
        System.out.println("SIGN UP");
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
        System.out.println("EDIT ACCOUNT");
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
        System.out.println("GET USERS");
        return userRepository.findAll();
    }
}
