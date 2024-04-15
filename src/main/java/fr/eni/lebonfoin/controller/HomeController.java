package fr.eni.lebonfoin.controller;

import fr.eni.lebonfoin.entity.Article;
import fr.eni.lebonfoin.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;  // Correct import for Spring's Model
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ArticleService articleService;

    @Autowired
    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }



    @GetMapping("/")
    public String getAllArticles(Model model) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Article> articles = articleService.getAllArticles().stream()
                .filter(article -> {
                    LocalDateTime debut = LocalDate.parse(article.getDateDebutEncheres(), formatter).atStartOfDay();
                    LocalDateTime fin = LocalDate.parse(article.getDateFinEncheres(), formatter).atTime(23, 59, 59); // Fin de la journée
                    return debut.isBefore(now) && fin.isAfter(now);
                })
                .collect(Collectors.toList());
        model.addAttribute("articles", articles);
        return "articlesIndex";
    }
}
