package WAT.I8E2S4.TaskManager.Model;

import WAT.I8E2S4.TaskManager.Model.Category;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
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
    @Getter @Setter Category category;
}
