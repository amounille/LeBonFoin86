package fr.eni.lebonfoin.repository;

import fr.eni.lebonfoin.entity.Enchere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EnchereRepository extends JpaRepository<Enchere, Long> {
    Enchere findByNoArticle(Long articleNo);
}