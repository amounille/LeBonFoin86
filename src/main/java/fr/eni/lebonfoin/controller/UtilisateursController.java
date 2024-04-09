package fr.eni.lebonfoin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/utilisateurs")
public class UtilisateursController {

    @GetMapping("/creer")
    public String getUtilisateurForm() {
        return "utilisateursCreation";
    }
}

