package fr.eni.lebonfoin.controller;

import java.util.List;
import java.util.Optional;

import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.UserRepository;
import fr.eni.lebonfoin.service.UserService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
public class UserController {

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;
    @Column(name = "credit", nullable = false)
    private Integer credit = 0; // Définir une valeur par défaut ici
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserService userService;


    @GetMapping("/registration")
    public String getRegistrationPage(User user, Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    @PostMapping("/registration")
    public String saveUser(User user, Model model) {
        // Encoder le mot de passe avec bcrypt avant de le sauvegarder
        String encodedPassword = passwordEncoder.encode(user.getMotDePasse());
        user.setMotDePasse(encodedPassword);

        userRepository.save(user);
        model.addAttribute("message", "Submitted Successfully");
        return "register";
    }



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

    @GetMapping("/profil")
    public String userProfil(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Optional<User> userOptional = userRepository.findByPseudo(username);
            if (userOptional.isPresent()) {
                model.addAttribute("user", userOptional.get());
                return "profil";
            } else {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
    }



    @GetMapping("/edit-profil")
    public String editProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> currentUserOptional = userRepository.findByPseudo(username);

        if (!currentUserOptional.isPresent()) {
            // Gérer l'absence d'utilisateur, par exemple en redirigeant vers une page d'erreur ou de login
            return "redirect:/login";
        }

        User currentUser = currentUserOptional.get();
        model.addAttribute("user", currentUser);
        return "edit-profil";
    }


    // Méthode pour gérer la soumission du formulaire de modification
    @PostMapping("/edit-profil")
    public String updateProfile(@ModelAttribute User user, Model model) {
        // Assurez-vous de crypter le mot de passe si vous permettez sa modification
        userRepository.save(user);
        model.addAttribute("message", "Profil mis à jour avec succès !");
        return "redirect:/profil";
    }
}