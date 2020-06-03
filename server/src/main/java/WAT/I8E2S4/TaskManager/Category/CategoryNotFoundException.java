package WAT.I8E2S4.TaskManager.Category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException() {
        this("Category not found!");
    }

    public CategoryNotFoundException(String message) {
        super(message, null);
    }

    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
