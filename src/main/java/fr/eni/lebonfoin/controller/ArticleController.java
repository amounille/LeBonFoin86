package fr.eni.lebonfoin.controller;

import fr.eni.lebonfoin.entity.Article;
import fr.eni.lebonfoin.entity.Categorie;
import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.ArticleRepository;
import fr.eni.lebonfoin.repository.CategorieRepository;
import fr.eni.lebonfoin.repository.UserRepository;
import fr.eni.lebonfoin.service.ArticleService;
import fr.eni.lebonfoin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategorieRepository categorieRepository;

    private final ArticleService articleService;
    private final UserService userService;

    @Autowired
    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/new/article")
    public String getArticleNew(Model model) {
        model.addAttribute("article", new Article());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByPseudo(username);
        Long userId = user.getNoUtilisateur();

        List<Categorie> categories = categorieRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("userId", userId);
        model.addAttribute("username", username);
        return "articleNew";
    }

    @PostMapping("/new/article")
    public String saveArticle(@RequestParam("categoryId") String categoryId, Article article, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByPseudo(username);
        Long userId = user.getNoUtilisateur();
        article.setNoUtilisateur(userId);
        Categorie categorie = categorieRepository.findById(Long.valueOf(categoryId)).orElseThrow(() -> new IllegalArgumentException("Catégorie non trouvée"));
        article.setNoCategorie(categorie.getNo_categorie());

        articleRepository.save(article);

        model.addAttribute("article", article);

        return "articleNew";
    }

    @PostMapping(value = "/article/{noArticle}", params = "_method=delete")
    public String deleteArticleHiddenMethod(@PathVariable("noArticle") Long articleNo) {
        return deleteArticle(articleNo);
    }

    @DeleteMapping("/article/{noArticle}")
    public String deleteArticle(@PathVariable("noArticle") Long articleNo) {
        Article articleToDelete = articleRepository.findById(articleNo)
                .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec le numéro : " + articleNo));
        articleRepository.delete(articleToDelete);
        return "redirect:/articles";
    }

    @GetMapping("/articles")
    public String getAllArticles(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "articles";
    }
}
