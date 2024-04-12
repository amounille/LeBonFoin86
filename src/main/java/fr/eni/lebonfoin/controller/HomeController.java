package fr.eni.lebonfoin.controller;

import fr.eni.lebonfoin.entity.Article;
import fr.eni.lebonfoin.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;  // Correct import for Spring's Model
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ArticleService articleService;

    @Autowired
    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }



    @GetMapping("/")
    public String getAllArticles(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "articlesIndex";
    }


}
