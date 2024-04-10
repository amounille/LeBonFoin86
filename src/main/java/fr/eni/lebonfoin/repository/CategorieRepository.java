package fr.eni.lebonfoin.repository;

import fr.eni.lebonfoin.entity.Article;
import fr.eni.lebonfoin.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

}