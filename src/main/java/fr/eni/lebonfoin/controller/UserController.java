package fr.eni.lebonfoin.controller;

import java.util.List;

import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.UserRepository;
import fr.eni.lebonfoin.service.UserService;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class UserController {

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

    //Récupérer l'utilisateur connecté
    @GetMapping("/profil")
    public String userProfil(Model model) {
        // Récupérer l'objet Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Vérifier si l'utilisateur est authentifié
        if (authentication != null && authentication.isAuthenticated()) {


            // Récupérer le nom d'utilisateur de l'utilisateur connecté
            String nom = authentication.getName();

            // Récupérer le profil de l'utilisateur à partir de la base de données
            User user = userRepository.findByPseudo(nom);

            // Passer les informations de l'utilisateur à la vue
            model.addAttribute("user", user);

            // Afficher la vue du profil de l'utilisateur
            return "profil";
        } else {
            // L'utilisateur n'est pas connecté, vous pouvez gérer cela comme vous le souhaitez
            // Par exemple, rediriger vers la page de connexion
            return "redirect:/login";
        }
    }

    //modification du profil
    @PostMapping("/modifierProfil")
    public String modifierProfil(@ModelAttribute("userForm") User userForm, Model model, RedirectAttributes redirectAttributes) {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nom = authentication.getName();
        User user = userRepository.findByPseudo(nom);

        // Vérifier si l'utilisateur existe
        if (user == null) {
            return "redirect:/profil";
        }

        // Mettre à jour les informations de l'utilisateur avec les données du formulaire
        user.setPseudo(userForm.getPseudo());
        user.setNom(userForm.getNom());
        user.setEmail(userForm.getEmail());
        user.setTelephone(userForm.getTelephone());
        user.setRue(userForm.getRue());
        user.setCodePostal(userForm.getCodePostal());
        user.setVille(userForm.getVille());

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Une erreur s'est produite lors de l'enregistrement des modifications. Veuillez réessayer.");
            return "error";
        }

        redirectAttributes.addFlashAttribute("message", "Modification réussie");

        // Rediriger vers la page de confirmation
        return "redirect:/confirmation";
    }
    //Page de confirmation
    @GetMapping("/confirmation")
    public String confirmationPage() {
        return "confirmation";
    }
}