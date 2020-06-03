package WAT.I8E2S4.TaskManager.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private long id;
    private String name;
    private String description;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime startDateTime;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime endDateTime;
    private boolean active;
    private boolean notification;
    private Long categoryId;

    public TaskResponse(Task task){
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.startDateTime = task.getStartDateTime();
        this.endDateTime = task.getEndDateTime();
        this.active = task.isActive();
        this.notification = task.isNotification();
        this.categoryId = task.getCategory().getId();
    }

    public static List<TaskResponse> makeList(List<Task> taskList) {
        ArrayList<TaskResponse> result = new ArrayList<>(taskList.size());
        for (Task task : taskList) {
            result.add(new TaskResponse(task));
        }
        return result;
    }
}
