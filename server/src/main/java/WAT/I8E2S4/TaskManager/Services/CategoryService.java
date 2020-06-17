package WAT.I8E2S4.TaskManager.Services;

import WAT.I8E2S4.TaskManager.Category.*;
import WAT.I8E2S4.TaskManager.Exceptions.CategoryExceptions.CategoryAlreadyExistsException;
import WAT.I8E2S4.TaskManager.Exceptions.CategoryExceptions.CategoryNoDataException;
import WAT.I8E2S4.TaskManager.Exceptions.CategoryExceptions.CategoryNotFoundException;
import WAT.I8E2S4.TaskManager.Repositories.CategoryRepository;
import WAT.I8E2S4.TaskManager.Repositories.TaskRepository;
import WAT.I8E2S4.TaskManager.User.User;
import WAT.I8E2S4.TaskManager.Exceptions.UserExceptions.UserNotFoundException;
import WAT.I8E2S4.TaskManager.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    public void addCategory(CategoryRequestAndResponse category, String username){
        if(category.getName()=="") throw new CategoryNoDataException();
        User user = userRepository.findByUsername(username).orElseThrow(
                ()->new UserNotFoundException()
        );
        categoryRepository.findByNameAndUserId(category.getName(), user.getId()).ifPresent(
                (exCat)->{
                    throw new CategoryAlreadyExistsException();
                }
        );
        Category result = Category.builder()
                .name(category.getName())
                .description(category.getDescription())
                .user(user).build();
        categoryRepository.save(result);
    }

    public List<CategoryRequestAndResponse> getCategories(String username){
        return CategoryRequestAndResponse.makeList(categoryRepository.findAllByUserUsername(username));
    }

    public void editCategory(Category editedCategory, String categoryName, String username){
        if(editedCategory.getName()=="") throw new CategoryNoDataException();
        Category dbCategory = categoryRepository.findByNameAndUserUsername(categoryName,username)
                .orElseThrow(
                        ()-> new CategoryNotFoundException("Category not found!")
                );
        if(editedCategory.getName() != null){
            dbCategory.setName(editedCategory.getName());
        }
        if(editedCategory.getDescription() != null){
            dbCategory.setDescription(editedCategory.getDescription());
        }
        categoryRepository.save(dbCategory);
    }

    public void deleteCategory(@PathVariable String categoryName, @PathVariable String username){
        Category dbCategory = categoryRepository.findByNameAndUserUsername(categoryName,username)
                .orElseThrow(
                        ()-> new CategoryNotFoundException("category not found!")
                );
        categoryRepository.delete(dbCategory);
    }
}
