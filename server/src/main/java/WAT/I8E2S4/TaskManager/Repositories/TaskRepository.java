package WAT.I8E2S4.TaskManager.Repositories;

import WAT.I8E2S4.TaskManager.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByCategory_IdAndCategory_User_username(long categoryId, String username);
    List<Task> findAllByCategory_NameAndCategory_User_username(String categoryName, String username);
    Optional<Task> findByIdAndCategory_IdAndCategory_User_Username(long id, long categoryId, String username);
    Optional<Task> findByIdAndCategory_NameAndCategory_User_Username(long id, String categoryName, String username);
}
