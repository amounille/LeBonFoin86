package fr.eni.lebonfoin.controller;

import org.springframework.ui.Model;  // Correct import for Spring's Model
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Récupère le nom de l'utilisateur connecté
        model.addAttribute("username", username);
        return "home";
    }
}
