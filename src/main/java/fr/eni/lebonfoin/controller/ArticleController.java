package fr.eni.lebonfoin.controller;

import fr.eni.lebonfoin.entity.Article;
import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.ArticleRepository;
import fr.eni.lebonfoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/new/article")
    public String getRegistrationPage(Model model) {
        model.addAttribute("article", new Article());
        return "article";
    }

    @PostMapping("/new/article")
    public String saveUser(Article article, Model model) {
        articleRepository.save(article);
        model.addAttribute("message", "Article mis en vente");
        return "article";
    }
}
