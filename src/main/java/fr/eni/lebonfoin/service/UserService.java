package fr.eni.lebonfoin.service;

import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    // Injection de JavaMailSender et UserRepository par constructeur
    @Autowired
    public UserService(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        user.setResetToken(token);
        user.setTokenCreationDate(new Date()); // Enregistre la date de création
        userRepository.save(user);
    }

    public void sendResetToken(User user, String applicationUrl) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Réinitialisation du mot de passe");
        mailMessage.setText("Pour réinitialiser votre mot de passe, cliquez sur le lien suivant : "
                + applicationUrl + "/reset?token=" + user.getResetToken());
        this.mailSender.send(mailMessage);
    }

    public User getUserByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    public void updatePassword(User user, String newPassword) {
        user.setMotDePasse(newPassword);
        user.setResetToken(null);
        user.setTokenCreationDate(null);
        userRepository.save(user);
    }


}
