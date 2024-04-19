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
@RequestMapping("/auth") // Route de base pour toutes les actions liées à l'authentification
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Afficher le formulaire pour demander la réinitialisation du mot de passe
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        // Retourne le nom du template Thymeleaf pour rendre le formulaire
        return "request-reset-password";
    }

    // Traiter la soumission du formulaire de demande de réinitialisation du mot de passe
    @PostMapping("/forgot")
    public String forgotPassword(@RequestParam String email, HttpServletRequest request, Model model) {
        // Rechercher l'utilisateur par email
        User user = userRepository.findByEmail(email);
        // Message à afficher, indépendamment de la présence ou non de l'utilisateur (mesure de sécurité)
        String message = "Si un compte avec cette adresse e-mail existe, nous avons envoyé un lien de réinitialisation.";

        if (user != null) {
            // Générer un jeton de réinitialisation unique
            String token = UUID.randomUUID().toString();
            // Créer un jeton de réinitialisation de mot de passe pour l'utilisateur
            userService.createPasswordResetTokenForUser(user, token);
            // Construire l'URL que l'utilisateur utilisera pour réinitialiser son mot de passe
            String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/auth/new-password?token=" + token;
            userService.sendResetToken(user, appUrl);
        }
        model.addAttribute("message", message);
        return "request-reset-password";
    }

    // Afficher le formulaire pour saisir le nouveau mot de passe
    @GetMapping("/new-password")
    public String showNewPasswordForm(@RequestParam String token, Model model) {
        /*model.addAttribute("token", token);*/
        return "new-password";
    }

    // Traiter la réinitialisation du mot de passe avec le jeton et le nouveau mot de passe
   /* @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        // Vérifier si le jeton est valide et correspond à un utilisateur
        User user = userService.getUserByResetToken(token);
        if (user == null) {
            // Retourner une réponse d'erreur si le jeton est invalide
            return new ResponseEntity<>("Token invalide.", HttpStatus.BAD_REQUEST);
        }
        // Mettre à jour le mot de passe de l'utilisateur
        userService.updatePassword(user, newPassword);
        // Retourner une réponse indiquant que l'opération a été un succès
        return new ResponseEntity<>("Mot de passe mis à jour.", HttpStatus.OK);
    }*/

}
