package pl.edu.wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.wypozyczalnia.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
