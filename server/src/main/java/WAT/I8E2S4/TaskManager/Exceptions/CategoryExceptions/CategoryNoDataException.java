package WAT.I8E2S4.TaskManager.Exceptions.CategoryExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class CategoryNoDataException extends RuntimeException{
    public CategoryNoDataException(){
        this("Category name is empty!");
    }

    public CategoryNoDataException(String message){
        super(message, null);
    }

    public CategoryNoDataException(String message, Throwable cause){
        super(message, cause);
    }
}
