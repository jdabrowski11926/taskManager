package WAT.I8E2S4.TaskManager.Exceptions.UserExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(){
        this("Username is already taken!");
    }

    public UserAlreadyExistsException(String message){
        super(message, null);
    }

    public UserAlreadyExistsException(String message, Throwable cause){
        super(message, cause);
    }

}
