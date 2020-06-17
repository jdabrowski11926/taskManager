package WAT.I8E2S4.TaskManager.Exceptions.UserExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
        this("User not found!");
    }

    public UserNotFoundException(String message){
        super(message, null);
    }

    public UserNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
