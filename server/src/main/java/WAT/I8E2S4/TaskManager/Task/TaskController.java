package WAT.I8E2S4.TaskManager.Task;

import WAT.I8E2S4.TaskManager.Category.Category;
import WAT.I8E2S4.TaskManager.Category.CategoryNotFoundException;
import WAT.I8E2S4.TaskManager.Category.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user/{username}/category/{categoryName}/task")
@AllArgsConstructor
public class TaskController {
    private TaskRepository taskRepository;
    private CategoryRepository categoryRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTask(@RequestBody TaskRequest task, @PathVariable String username, @PathVariable String categoryName){
        if(task.getName()=="") throw new TaskNoDataException();
        if(task.getStartDateTime().isAfter(task.getEndDateTime())) throw  new TaskNoDataException("Inserted invalid data, start date is later than end date");
        Task result = Task.builder()
                .name(task.getName())
                .description(task.getDescription())
                .startDateTime(task.getStartDateTime())
                .endDateTime((task.getEndDateTime()))
                .active(task.isActive())
                .notification(task.isNotification())
                .category(
                        categoryRepository.findByNameAndUserUsername(categoryName, username)
                                .orElseThrow(()->new CategoryNotFoundException())
                ).build();
        taskRepository.save(result);
    }

    @GetMapping()
    public List<TaskResponse> getTasksByCategory(@PathVariable String username, @PathVariable String categoryName){
        return TaskResponse.makeList(taskRepository.findAllByCategory_NameAndCategory_User_username(categoryName, username));
    }

    @PutMapping("/{id}")
    public void editTask(@PathVariable String username, @PathVariable String categoryName, @PathVariable long id, @RequestBody TaskRequestEdit task){
        if(task.getName()=="") throw new TaskNoDataException();
        if(task.getStartDateTime().isAfter(task.getEndDateTime())) throw  new TaskNoDataException("Inserted invalid data, start date is later than end date");
        Task dbTask = taskRepository.findByIdAndCategory_NameAndCategory_User_Username(id,categoryName,username)
                .orElseThrow(()->new TaskNotFoundException());
        if(task.getName()!=null) dbTask.setName(task.getName());
        if(task.getDescription()!=null) dbTask.setDescription(task.getDescription());
        if(task.getStartDateTime()!=null) dbTask.setStartDateTime(task.getStartDateTime());
        if(task.getEndDateTime()!=null) dbTask.setEndDateTime(task.getEndDateTime());
        dbTask.setActive(task.isActive());
        dbTask.setNotification(task.isNotification());
        if(task.getCategoryId()!=null) {
            Category newCategory = categoryRepository.findByIdAndUserUsername(task.getCategoryId(), username)
                    .orElseThrow(()->new CategoryNotFoundException());
        }
        taskRepository.save(dbTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id, @PathVariable String username, @PathVariable String categoryName){
        Task task = taskRepository.findByIdAndCategory_NameAndCategory_User_Username(id,categoryName, username)
                .orElseThrow(()->new TaskNotFoundException());
        taskRepository.delete(task);
    }
}
