package fr.eni.lebonfoin.controller;

import fr.eni.lebonfoin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordController {

    @Autowired
    private UserService userService;

    @RequestMapping("/reset-password")
    public String displayResetPasswordPage() {
        return "reset-password";
    }


    @PostMapping("/requestReset")
    public String requestPasswordReset(@RequestParam("email") String userEmail) {
        userService.requestPasswordReset(userEmail);
        return "checkYourEmail";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String newPassword) {
        if (userService.resetPassword(token, newPassword)) {
            return "successResetPassword";
        }
        return "errorResetPassword";
    }
}

