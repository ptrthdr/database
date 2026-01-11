package pl.edu.wypozyczalnia.controller;

import org.springframework.web.bind.annotation.*;
import pl.edu.wypozyczalnia.model.User;
import pl.edu.wypozyczalnia.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
