package fr.eni.lebonfoin.repository;

import fr.eni.lebonfoin.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByNoCategorie(Long noCategorie);
    List<Article> findByNoCategorieAndNomArticleContainingIgnoreCase(Long noCategorie, String nomArticle);
    List<Article> findByNomArticleContainingIgnoreCase(String nomArticle);
}