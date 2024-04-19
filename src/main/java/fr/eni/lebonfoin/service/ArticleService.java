package fr.eni.lebonfoin.service;

import fr.eni.lebonfoin.entity.Article;
import fr.eni.lebonfoin.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // Ajout de la méthode pour filtrer les articles par catégorie
    public List<Article> findArticlesByCategory(Long categoryId) {
        return articleRepository.findByNoCategorie(categoryId);
    }

    public List<Article> findArticlesByCategoryAndText(Long categoryId, String searchText) {
        return articleRepository.findByNoCategorieAndNomArticleContainingIgnoreCase(categoryId, searchText);
    }

    public List<Article> findArticlesByText(String searchText) {
        return articleRepository.findByNomArticleContainingIgnoreCase(searchText);
    }


}
