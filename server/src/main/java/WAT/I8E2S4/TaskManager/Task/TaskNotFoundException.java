package WAT.I8E2S4.TaskManager.Task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(){
        this("Task not found");
    }

    public TaskNotFoundException(String message){
        super(message, null);
    }

    public TaskNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
