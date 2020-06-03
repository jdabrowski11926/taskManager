package WAT.I8E2S4.TaskManager.Task;

import WAT.I8E2S4.TaskManager.Category.Category;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private long id;

    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Getter @Setter private LocalDateTime startDateTime;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Getter @Setter private LocalDateTime endDateTime;
    @Getter @Setter private boolean active;
    @Getter @Setter private boolean notification;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    @Getter @Setter Category category;

    public Task(String name, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean active, boolean notification){
        this.name = name;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.active = active;
        this.notification = notification;
    }
}
