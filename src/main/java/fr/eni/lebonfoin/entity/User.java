package fr.eni.lebonfoin.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UTILISATEURS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no_utilisateur")
    private Long id;

    @Column(name = "pseudo", unique = true, nullable = false, length = 30)
    private String username;

    @Column(name = "nom", nullable = false, length = 30)
    private String lastName;

    @Column(name = "prenom", nullable = false, length = 30)
    private String firstName;

    @Column(name = "email", unique = true, nullable = false, length = 20)
    private String email;

    @Column(name = "telephone", length = 15)
    private String phoneNumber;

    @Column(name = "rue", nullable = false, length = 30)
    private String street;

    @Column(name = "code_postal", nullable = false, length = 10)
    private String postalCode;

    @Column(name = "ville", nullable = false, length = 30)
    private String city;

    @Column(name = "mot_de_passe", nullable = false, length = 30)
    private String password;

    @Column(name = "credit", nullable = false)
    private Integer credit;

    @Column(name = "administrateur", nullable = false)
    private boolean isAdmin;

    // Fields for password reset
    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "token_creation_date")
    private LocalDateTime tokenCreationDate;

    // Getters and setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Integer getCredit() { return credit; }
    public void setCredit(Integer credit) { this.credit = credit; }
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    public LocalDateTime getTokenCreationDate() { return tokenCreationDate; }
    public void setTokenCreationDate(LocalDateTime tokenCreationDate) { this.tokenCreationDate = tokenCreationDate; }
}
