package WAT.I8E2S4.TaskManager.Services;

import WAT.I8E2S4.TaskManager.Repositories.CategoryRepository;
import WAT.I8E2S4.TaskManager.Exceptions.UserExceptions.InvalidOldPasswordException;
import WAT.I8E2S4.TaskManager.Exceptions.UserExceptions.UserAlreadyExistsException;
import WAT.I8E2S4.TaskManager.Exceptions.UserExceptions.UserNoDataException;
import WAT.I8E2S4.TaskManager.Exceptions.UserExceptions.UserNotFoundException;
import WAT.I8E2S4.TaskManager.Repositories.TaskRepository;
import WAT.I8E2S4.TaskManager.Repositories.UserRepository;
import WAT.I8E2S4.TaskManager.User.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private CategoryRepository categoryRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailController emailController;

    public void signUp(User user){
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

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void editAccount(String username, UserEditPassword editPassword){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
        String oldPassword = editPassword.getOldPassword();
        String newPassword = editPassword.getNewPassword();
        if(newPassword=="") throw new UserNoDataException("Password can not be empty");
        if(!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
            throw new InvalidOldPasswordException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }



}
