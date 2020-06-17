package WAT.I8E2S4.TaskManager.Exceptions.CategoryExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(){
        this("Category already exists!");
    }

    public CategoryAlreadyExistsException(String message){
        super(message,null);
    }

    public CategoryAlreadyExistsException(String message, Throwable cause){
        super(message, cause);
    }

}