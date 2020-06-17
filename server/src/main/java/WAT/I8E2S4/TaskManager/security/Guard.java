package WAT.I8E2S4.TaskManager.Security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import WAT.I8E2S4.TaskManager.Model.User;
import WAT.I8E2S4.TaskManager.Exceptions.UserExceptions.UserNotFoundException;
import WAT.I8E2S4.TaskManager.Repositories.UserRepository;

@Component
@AllArgsConstructor
public class Guard {
    private UserRepository userRepository;

    public boolean checkUsername(Authentication auth, String name){
        User user = userRepository.findByUsername(name).orElseThrow(
                UserNotFoundException::new
        );
        return user.getUsername().equals(auth.getName());
    }
}
