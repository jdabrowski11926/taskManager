package WAT.I8E2S4.TaskManager.Category;

import WAT.I8E2S4.TaskManager.User.User;
import WAT.I8E2S4.TaskManager.Task.Task;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private long id;

    @Getter @Setter private String name;
    @Getter @Setter private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<Task> tasks;

    @ManyToOne()
    @Getter @Setter User user;

    public Category(String name, User user){
        this.name = name;
        this.user = user;
    }
}