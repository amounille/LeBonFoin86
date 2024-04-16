package fr.eni.lebonfoin.controller;

import java.util.List;
import java.util.Optional;

import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.UserRepository;
import fr.eni.lebonfoin.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute User user, Model model) {
        String encodedPassword = passwordEncoder.encode(user.getMotDePasse());
        user.setMotDePasse(encodedPassword);
        userRepository.save(user);
        model.addAttribute("message", "Inscription réussie !");
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/profil")
    public String userProfil(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> userOptional = userRepository.findByPseudo(username);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "profil";
        }
        return "redirect:/login";
    }


    @GetMapping("/edit-profil")
    public String editProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByPseudo(username);
        if (!user.isPresent()) {
            return "redirect:/login";
        }
        model.addAttribute("user", user.get());
        return "edit-profil";
    }

    @PostMapping("/edit-profil")
    @Transactional
    public String updateProfile(@ModelAttribute User updatedUser, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> result = userRepository.findByPseudo(username);

        if (!result.isPresent()) {
            return "redirect:/login";
        }

        User existingUser = result.get();
        System.out.println("Updating user: " + existingUser.getPseudo() + " with new data from " + updatedUser.getPseudo());

        updateExistingUser(existingUser, updatedUser);
        userRepository.save(existingUser);

        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        model.addAttribute("message", "Profil mis à jour avec succès !");

        return "redirect:/profil";
    }

    private void updateExistingUser(User existingUser, User updatedUser) {
        System.out.println("Updating details from " + existingUser.getPseudo() + " to " + updatedUser.getPseudo());
        existingUser.setPseudo(updatedUser.getPseudo()); // updating pseudo as well
        existingUser.setNom(updatedUser.getNom());
        existingUser.setPrenom(updatedUser.getPrenom());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setTelephone(updatedUser.getTelephone());
        existingUser.setRue(updatedUser.getRue());
        existingUser.setCodePostal(updatedUser.getCodePostal());
        existingUser.setVille(updatedUser.getVille());
        if (updatedUser.getMotDePasse() != null && !updatedUser.getMotDePasse().isEmpty()) {
            existingUser.setMotDePasse(passwordEncoder.encode(updatedUser.getMotDePasse()));
        }
    }
}