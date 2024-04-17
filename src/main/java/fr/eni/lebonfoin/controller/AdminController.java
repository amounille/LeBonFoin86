package fr.eni.lebonfoin.controller;

import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/show/user")
    public String getAllUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByPseudo(username);
        if (currentUser != null && currentUser.isAdministrateur()) {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            return "userShow";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/delete/user/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/show/user";
    }

}
