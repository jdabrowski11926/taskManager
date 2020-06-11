package WAT.I8E2S4.TaskManager.security;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import WAT.I8E2S4.TaskManager.User.User;
import WAT.I8E2S4.TaskManager.User.UserNotFoundException;
import WAT.I8E2S4.TaskManager.User.UserRepository;

@Component
public class Guard {
    private UserRepository userRepository;

    public boolean checkUsername(Authentication auth, String name){
        User user = userRepository.findByUsername(name).orElseThrow(
                ()-> new UserNotFoundException()
        );
        return user.getUsername() == auth.getName();
    }
}
