package fr.eni.lebonfoin.controller;

import java.util.List;
import java.util.Optional;

import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.UserRepository;
import fr.eni.lebonfoin.service.ArticleService;
import fr.eni.lebonfoin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.hibernate.service.spi.ServiceException;
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
    @Autowired
    private ArticleService articleService;


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
        // Obtient l'authentification actuelle pour récupérer le nom d'utilisateur
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Récupère l'utilisateur à partir du nom d'utilisateur
        User user = userRepository.findByPseudo(username);

        // Vérifie si l'utilisateur existe
        if (user != null) {
            // Ajoute l'utilisateur au modèle pour affichage
            model.addAttribute("user", user);
            // Retourne la vue du profil de l'utilisateur
            return "profil";
        }

        // Redirige vers la page de connexion si l'utilisateur n'est pas trouvé
        return "redirect:/login";
    }



    @GetMapping("/edit-profil")
    public String editProfile(Model model) {
        // Obtient l'authentification actuelle pour récupérer le nom d'utilisateur
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Récupère l'utilisateur à partir du nom d'utilisateur
        User user = userRepository.findByPseudo(username);

        // Vérifie si l'utilisateur existe
        if (user != null) {
            // Ajoute l'utilisateur au modèle pour affichage dans la page d'édition
            model.addAttribute("user", user);
            // Retourne la vue pour éditer le profil
            return "edit-profil";
        }
        // Redirige vers la page de connexion si l'utilisateur n'est pas trouvé
        return "redirect:/login";
    }

    @PostMapping("/edit-profil")
    @Transactional
    public String updateProfile(@ModelAttribute User updatedUser, Model model) {
        // Obtient l'authentification actuelle pour récupérer le nom d'utilisateur
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Récupère l'utilisateur existant à partir du nom d'utilisateur
        User existingUser = userRepository.findByPseudo(username);

        // Vérifie si l'utilisateur existe
        if (existingUser == null) {
            // Redirige vers la page de connexion si l'utilisateur n'est pas trouvé
            return "redirect:/login";
        }

        // Affiche un message de mise à jour dans la console
        System.out.println("Updating user: " + existingUser.getPseudo() + " with new data from " + updatedUser.getPseudo());

        // Met à jour les données de l'utilisateur existant avec les nouvelles données
        updateExistingUser(existingUser, updatedUser);
        // Sauvegarde les modifications de l'utilisateur dans la base de données
        userRepository.save(existingUser);
        // Ajoute un message au modèle pour indiquer que le profil a été mis à jour avec succès
        model.addAttribute("message", "Profil mis à jour avec succès !");

        // Redirige vers la page du profil de l'utilisateur
        return "redirect:/profil";
    }


    // Méthode privée pour mettre à jour les données de l'utilisateur existant avec les nouvelles données
    private void updateExistingUser(User existingUser, User updatedUser) {
        existingUser.setNom(updatedUser.getNom());
        existingUser.setPrenom(updatedUser.getPrenom());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setTelephone(updatedUser.getTelephone());
        existingUser.setRue(updatedUser.getRue());
        existingUser.setCodePostal(updatedUser.getCodePostal());
        existingUser.setVille(updatedUser.getVille());

        // Vérifie si un nouveau mot de passe est fourni et le met à jour en conséquence
        if (updatedUser.getMotDePasse() != null && !updatedUser.getMotDePasse().isEmpty()) {
            existingUser.setMotDePasse(passwordEncoder.encode(updatedUser.getMotDePasse()));
        }
    }

}