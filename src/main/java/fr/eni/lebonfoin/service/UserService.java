package fr.eni.lebonfoin.service;

import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List; // Pour List
import org.springframework.mail.SimpleMailMessage; // Pour SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender; // Pour JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // Generate and set a reset token with expiration.
            String token = UUID.randomUUID().toString();
            Date tokenExpiration = new Date(System.currentTimeMillis() + 3600000); // 1 hour
            user.setResetToken(token);
            user.setResetTokenExpiration(tokenExpiration);
            userRepository.save(user);

            // Construct and send the password reset email.
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("rick.bouyaghi2023@campus-eni.fr");
            message.setTo(user.getEmail());
            message.setSubject("MAIL ENVOYE");
            message.setText("To reset your password, click the link below:\n"
                    + "http://localhost:8080/reset-password?token=" + token);
            mailSender.send(message);
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token);
        if (user != null && user.getResetTokenExpiration().after(new Date())) {
            // Reset password and clear reset token.
            user.setMotDePasse(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiration(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
