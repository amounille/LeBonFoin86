package fr.eni.lebonfoin.controller;

import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.UserRepository;
import fr.eni.lebonfoin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Ajout d'une méthode GET pour afficher le formulaire de réinitialisation
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "request-reset-password";
    }

    @PostMapping("/forgot")
    public String forgotPassword(@RequestParam String email, HttpServletRequest request, Model model) {
        User user = userRepository.findByEmail(email);
        // Indiquer un message générique pour éviter l'énumération des comptes
        String message = "Si un compte avec cette adresse e-mail existe, nous avons envoyé un lien de réinitialisation.";
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            String appUrl = request.getScheme() + "://" + request.getServerName();
            userService.sendResetToken(user, appUrl);
        }
        model.addAttribute("message", message);
        return "request-reset-password"; // Reste sur la même page et affiche le message
    }



    // Méthode POST existante pour réinitialiser le mot de passe
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        User user = userService.getUserByResetToken(token);
        if (user == null) {
            return new ResponseEntity<>("Token invalide.", HttpStatus.BAD_REQUEST);
        }
        userService.updatePassword(user, newPassword);
        return new ResponseEntity<>("Mot de passe mis à jour.", HttpStatus.OK);
    }
}
