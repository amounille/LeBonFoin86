package fr.eni.lebonfoin.controller;

import fr.eni.lebonfoin.entity.Article;
import fr.eni.lebonfoin.entity.Categorie;
import fr.eni.lebonfoin.service.ArticleService;
import fr.eni.lebonfoin.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ArticleService articleService;
    private final CategorieService categorieService;

    @Autowired
    public HomeController(ArticleService articleService, CategorieService categorieService) {
        this.articleService = articleService;
        this.categorieService = categorieService;
    }

    @GetMapping("/")
    public String getAllArticles(Model model) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Article> articles = articleService.getAllArticles().stream()
                .filter(article -> {
                    LocalDateTime debut = LocalDate.parse(article.getDateDebutEncheres(), formatter).atStartOfDay();
                    LocalDateTime fin = LocalDate.parse(article.getDateFinEncheres(), formatter).atTime(23, 59, 59);
                    return debut.isBefore(now) && fin.isAfter(now);
                })
                .collect(Collectors.toList());

        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        return "articlesIndex";
    }


    @GetMapping(path = "/filter-articles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Article> filterArticles(@RequestParam(required = false) Long categoryId, @RequestParam(required = false) String searchText) {
        if (categoryId != null && categoryId > 0 && searchText != null && !searchText.isEmpty()) {
            return articleService.findArticlesByCategoryAndText(categoryId, searchText);
        } else if (categoryId != null && categoryId > 0) {
            return articleService.findArticlesByCategory(categoryId);
        } else if (searchText != null && !searchText.isEmpty()) {
            return articleService.findArticlesByText(searchText);
        } else {
            return articleService.getAllArticles();
        }
    }


}
