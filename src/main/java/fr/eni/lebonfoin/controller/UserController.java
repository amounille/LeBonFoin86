package fr.eni.lebonfoin.controller;

import java.util.List;

import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.UserRepository;
import fr.eni.lebonfoin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/registration")
    public String getRegistrationPage(User user, Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registration")
    public String saveUser(User user, Model model) {
        // Encoder le mot de passe avec bcrypt avant de le sauvegarder
        String encodedPassword = passwordEncoder.encode(user.getMotDePasse());
        user.setMotDePasse(encodedPassword);

        userRepository.save(user);
        model.addAttribute("message", "Submitted Successfully");
        return "register";
    }

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}