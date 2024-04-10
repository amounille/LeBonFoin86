package fr.eni.lebonfoin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UTILISATEURS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no_utilisateur")
    private Long noUtilisateur;

    @Column(name = "pseudo")
    private String pseudo;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "rue")
    private String rue;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "ville")
    private String ville;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Column(name = "credit")
    private String credit;

    @Column(name = "administrateur")
    private boolean administrateur;

    @Column(name = "reset_token")
    private String resetToken;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "token_creation_date")
    private Date tokenCreationDate;
}
