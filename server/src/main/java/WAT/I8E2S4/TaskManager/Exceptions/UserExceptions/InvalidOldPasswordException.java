package WAT.I8E2S4.TaskManager.Exceptions.UserExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidOldPasswordException extends RuntimeException{
    public InvalidOldPasswordException(){
        this("Invalid credentials");
    }

    public InvalidOldPasswordException(String message){
        super(message, null);
    }
}
