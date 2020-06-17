package WAT.I8E2S4.TaskManager.Services;

import WAT.I8E2S4.TaskManager.Model.User;
import WAT.I8E2S4.TaskManager.Model.UserEditPassword;
import WAT.I8E2S4.TaskManager.Exceptions.UserExceptions.InvalidOldPasswordException;
import WAT.I8E2S4.TaskManager.Exceptions.UserExceptions.UserAlreadyExistsException;
import WAT.I8E2S4.TaskManager.Exceptions.UserExceptions.UserNoDataException;
import WAT.I8E2S4.TaskManager.Exceptions.UserExceptions.UserNotFoundException;
import WAT.I8E2S4.TaskManager.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        if(editPassword.getNewPassword()=="") throw new UserNoDataException("Password can not be empty");
        if(!bCryptPasswordEncoder.matches(editPassword.getOldPassword(), user.getPassword())){
            throw new InvalidOldPasswordException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(editPassword.getNewPassword()));
        userRepository.save(user);
    }
}
