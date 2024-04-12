package fr.eni.lebonfoin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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


    public Long getNoCategorie() {
        return noCategorie;
    }

    public void setNoCategorie(Long noCategorie) {
        this.noCategorie = noCategorie;
    }

    public Long getNoUtilisateur() {
        return noUtilisateur;
    }

    public void setNoUtilisateur(Long noUtilisateur) {
        this.noUtilisateur = noUtilisateur;
    }

    public double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public double getPrixInitial() {
        return prixInitial;
    }

    public void setPrixInitial(double prixInitial) {
        this.prixInitial = prixInitial;
    }

    public String getDateFinEncheres() {
        return dateFinEncheres;
    }

    public void setDateFinEncheres(String dateFinEncheres) {
        this.dateFinEncheres = dateFinEncheres;
    }

    public String getDateDebutEncheres() {
        return dateDebutEncheres;
    }

    public void setDateDebutEncheres(String dateDebutEncheres) {
        this.dateDebutEncheres = dateDebutEncheres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNomArticle() {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public Long getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(Long noArticle) {
        this.noArticle = noArticle;
    }
}
