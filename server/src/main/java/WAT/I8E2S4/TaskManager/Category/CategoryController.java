package WAT.I8E2S4.TaskManager.Category;

import WAT.I8E2S4.TaskManager.Task.Task;
import WAT.I8E2S4.TaskManager.Task.TaskRepository;
import WAT.I8E2S4.TaskManager.User.User;
import WAT.I8E2S4.TaskManager.User.UserNotFoundException;
import WAT.I8E2S4.TaskManager.User.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user/{username}/category")
@AllArgsConstructor
public class CategoryController {
    private CategoryRepository categoryRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addCategory(@RequestBody CategoryRequestAndResponse category, @PathVariable String username){
        System.out.println("ADD CATEGORY");
        System.out.println("CATEGORY NAME "+category.getName());
        System.out.println("CATEGORY DESC "+category.getDescription());
        System.out.println("CATEGORY USER "+category.getUser());
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

    @GetMapping
    public List<CategoryRequestAndResponse> getCategories(@PathVariable String username){
        System.out.println("GET CATEGORIES");
        return CategoryRequestAndResponse.makeList(categoryRepository.findAllByUserUsername(username));
    }

    @PutMapping("/{categoryName}")
    public void editCategory(@RequestBody Category editedCategory, @PathVariable String categoryName, @PathVariable String username){
        System.out.println("EDIT CATEGORY");
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

    @DeleteMapping("/{categoryName}")
    public void deleteCategory(@PathVariable String categoryName, @PathVariable String username){
        System.out.println("DELETE CATEGORY");
        Category dbCategory = categoryRepository.findByNameAndUserUsername(categoryName,username)
                .orElseThrow(
                        ()-> new CategoryNotFoundException("category not found!")
                );
        categoryRepository.delete(dbCategory);
    }

    @PutMapping("/join")
    public void joinCategories(@RequestBody CategoryJoin categoryJoin, @PathVariable String username){
        Category sourceCategory = categoryRepository
                .findByIdAndUserUsername(categoryJoin.getSourceId(),username)
                .orElseThrow(
                        ()-> new CategoryNotFoundException(
                                "category \"" + categoryJoin.getSourceId() + "\" not found!"
                        )
                );
        Category targetCategory = categoryRepository
                .findByIdAndUserUsername(categoryJoin.getTargetId(), username)
                .orElseThrow(
                        ()-> new CategoryNotFoundException(
                                "category \"" + categoryJoin.getSourceId() + "\" not found!"
                        )
                );
        List<Task> tasks = taskRepository.findAllByCategory_IdAndCategory_User_username(categoryJoin.getTargetId(),username);
        if(categoryJoin.getTargetId()!=categoryJoin.getSourceId()){
            for(Task task: tasks){
                task.setCategory(sourceCategory);
                taskRepository.save(task);
            }
            categoryRepository.delete(targetCategory);
        }
    }
}