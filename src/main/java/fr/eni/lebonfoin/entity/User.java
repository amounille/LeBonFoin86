package fr.eni.lebonfoin.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "UTILISATEURS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noUtilisateur;

    private String pseudo;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String rue;
    private String codePostal;
    private String ville;
    private String motDePasse;
    private Integer credit; // Assuming credit should be an integer.
    private boolean administrateur;
    private String resetToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenCreationDate;
}
