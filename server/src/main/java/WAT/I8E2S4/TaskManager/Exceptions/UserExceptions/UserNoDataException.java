package WAT.I8E2S4.TaskManager.Exceptions.UserExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class UserNoDataException extends RuntimeException{
    public UserNoDataException(){
        this("Username or password is empty!");
    }

    public UserNoDataException(String message){
        super(message, null);
    }

    public UserNoDataException(String message, Throwable cause){
        super(message, cause);
    }
}
