package fr.eni.lebonfoin.entity;

import fr.eni.lebonfoin.entity.EnchereKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "ENCHERES")
@IdClass(EnchereKey.class)
public class Enchere {

    @Id
    private int noUtilisateur;

    @Id
    private int noArticle;

    @Column(name = "date_enchere")
    private LocalDateTime dateEnchere;

    @Column(name = "montant_enchere")
    private BigDecimal montantEnchere;
}
