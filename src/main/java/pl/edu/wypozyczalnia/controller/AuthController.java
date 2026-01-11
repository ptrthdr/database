package pl.edu.wypozyczalnia.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pl.edu.wypozyczalnia.model.User;
import pl.edu.wypozyczalnia.repository.UserRepository;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ===== FORMULARZ LOGOWANIA =====
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // ===== LOGOWANIE =====
    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .map(user -> {
                    session.setAttribute("loggedUser", user.getUsername());
                    return "redirect:/view/cars";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Invalid login or password");
                    return "login";
                });
    }

    // ===== FORMULARZ REJESTRACJI =====
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    // ===== REJESTRACJA =====
    @PostMapping("/register")
    public String register(@RequestParam String username,
            @RequestParam String password,
            Model model) {

        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));

        userRepository.save(user);
        return "redirect:/login";
    }

    // ===== WYLOGOWANIE =====
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
