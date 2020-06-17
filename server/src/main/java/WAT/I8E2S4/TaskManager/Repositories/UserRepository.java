package WAT.I8E2S4.TaskManager.Repositories;

import WAT.I8E2S4.TaskManager.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByUsername(String username);
}
