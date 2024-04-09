package fr.eni.lebonfoin.controller;

import java.util.List;

import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/registration")
    public String saveUser(User user, Model model) {
        userRepository.save(user);
        model.addAttribute("message", "Submitted Successfully");
        return "register";
    }

    @GetMapping("/users")
    public String usersPage(Model model) {
        List<User> listOfUsers = userRepository.findAll();
        model.addAttribute("user", listOfUsers);
        return "user";
    }
}