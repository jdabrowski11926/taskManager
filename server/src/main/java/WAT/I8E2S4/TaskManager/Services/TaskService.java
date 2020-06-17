package WAT.I8E2S4.TaskManager.Services;

import WAT.I8E2S4.TaskManager.Exceptions.CategoryExceptions.CategoryNotFoundException;
import WAT.I8E2S4.TaskManager.Model.Task;
import WAT.I8E2S4.TaskManager.Repositories.CategoryRepository;
import WAT.I8E2S4.TaskManager.Exceptions.TaskExceptions.TaskNoDataException;
import WAT.I8E2S4.TaskManager.Exceptions.TaskExceptions.TaskNotFoundException;
import WAT.I8E2S4.TaskManager.Repositories.TaskRepository;
import WAT.I8E2S4.TaskManager.Responses.TaskResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;
    private CategoryRepository categoryRepository;

    public void addTask(Task task, String username, String categoryName){
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

    public List<TaskResponse> getTasksByCategory(String username, String categoryName){
        return TaskResponse.makeList(taskRepository.findAllByCategory_NameAndCategory_User_username(categoryName, username));
    }

    public void editTask(String username, String categoryName, long id, Task task){
        if(task.getName()=="") throw new TaskNoDataException("Task name is empty");
        if(task.getStartDateTime()==null) throw new TaskNoDataException("Start date/time is empty");
        if(task.getEndDateTime()==null) throw new TaskNoDataException("End date/time is empty");
        if(task.getStartDateTime().isAfter(task.getEndDateTime())) throw  new TaskNoDataException("Inserted invalid data, start date is later than end date");

        Task dbTask = taskRepository.findByIdAndCategory_NameAndCategory_User_Username(id,categoryName,username)
                .orElseThrow(()->new TaskNotFoundException());
        dbTask.setName(task.getName());
        dbTask.setDescription(task.getDescription());
        dbTask.setStartDateTime(task.getStartDateTime());
        dbTask.setEndDateTime(task.getEndDateTime());
        dbTask.setActive(task.isActive());
        dbTask.setNotification(task.isNotification());
        //if(task.getCategory().getId()!=null) {
        //    Category newCategory = categoryRepository.findByIdAndUserUsername(task.getCategory().getId(), username)
        //            .orElseThrow(()->new CategoryNotFoundException());
        //}
        taskRepository.save(dbTask);
    }

    public void deleteTask(long id, String username, String categoryName){
        Task task = taskRepository.findByIdAndCategory_NameAndCategory_User_Username(id,categoryName, username)
                .orElseThrow(()->new TaskNotFoundException());
        taskRepository.delete(task);
    }
}