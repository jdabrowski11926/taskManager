package WAT.I8E2S4.TaskManager.Model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="category")
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private Long id;

    @Getter @Setter private String name;
    @Getter @Setter private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<Task> tasks;

    @ManyToOne()
    @JoinColumn(name="user_id")
    @Getter @Setter User user;
}