package WAT.I8E2S4.TaskManager.Task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class TaskNoDataException extends RuntimeException{
    public TaskNoDataException(){
        this("Task name is empty!");
    }

    public TaskNoDataException(String message){
        super(message, null);
    }

    public TaskNoDataException(String message, Throwable cause){
        super(message, cause);
    }
}

