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
@Table(name = "ARTICLES_VENDUS")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no_article")
    private Long noArticle;

    @Column(name = "nom_article")
    private String nomArticle;

    private String description;

    @Column(name = "date_debut_encheres")
    private String dateDebutEncheres;

    @Column(name = "date_fin_encheres")
    private String dateFinEncheres;

    @Column(name = "prix_initial")
    private double prixInitial;

    @Column(name = "prix_vente")
    private double prixVente;

    @Column(name = "no_utilisateur")
    private Long noUtilisateur;

    @Column(name = "no_categorie")
    private Long noCategorie;

}
