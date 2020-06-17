package WAT.I8E2S4.TaskManager.Controllers;

import WAT.I8E2S4.TaskManager.Model.Task;
import WAT.I8E2S4.TaskManager.Responses.TaskResponse;
import WAT.I8E2S4.TaskManager.Services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user/{username}/category/{categoryName}/task")
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTask(@RequestBody Task task, @PathVariable String username, @PathVariable String categoryName){
        taskService.addTask(task, username, categoryName);
    }

    @GetMapping()
    public List<TaskResponse> getTasksByCategory(@PathVariable String username, @PathVariable String categoryName){
        return taskService.getTasksByCategory(username, categoryName);
    }

    @PutMapping("/{id}")
    public void editTask(@PathVariable String username, @PathVariable String categoryName, @PathVariable long id, @RequestBody Task task){
        taskService.editTask(username, categoryName, id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id, @PathVariable String username, @PathVariable String categoryName){
        taskService.deleteTask(id, username, categoryName);
    }
}
