package WAT.I8E2S4.TaskManager.Repositories;

import WAT.I8E2S4.TaskManager.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndUserId(String name, long userId);
    Optional<Category> findByIdAndUserUsername(long id, String username);
    Optional<Category> findByNameAndUserUsername(String name, String username);
    List<Category> findAllByUserUsername(String username);
}
