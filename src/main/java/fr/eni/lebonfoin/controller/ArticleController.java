package fr.eni.lebonfoin.controller;

import fr.eni.lebonfoin.entity.Article;
import fr.eni.lebonfoin.entity.Categorie;
import fr.eni.lebonfoin.entity.Enchere;
import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.ArticleRepository;
import fr.eni.lebonfoin.repository.CategorieRepository;
import fr.eni.lebonfoin.repository.EnchereRepository;
import fr.eni.lebonfoin.repository.UserRepository;
import fr.eni.lebonfoin.service.ArticleService;
import fr.eni.lebonfoin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategorieRepository categorieRepository;
    @Autowired
    EnchereRepository enchereRepository;
    private final ArticleService articleService;


    @Autowired
    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;

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

    @GetMapping("/edit/article/{noArticle}")
    public String editArticle(@PathVariable("noArticle") Long articleNo, Model model) {
        Article article = articleRepository.findById(articleNo)
                .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec le numéro : " + articleNo));
        List<Categorie> categories = categorieRepository.findAll();

        model.addAttribute("article", article);
        model.addAttribute("categories", categories);
        return "articleEdit";
    }

    @PostMapping("/edit/article/{noArticle}")
    public String updateArticle(@PathVariable("noArticle") Long articleNo, @RequestParam("categoryId") String categoryId, Article updatedArticle) {
        Article articleToUpdate = articleRepository.findById(articleNo)
                .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec le numéro : " + articleNo));
        Long originalUserId = articleToUpdate.getNoUtilisateur();
        Double categoryIdAsDouble = Double.valueOf(categoryId.replace(",", "."));
        Long categoryIdAsLong = categoryIdAsDouble.longValue();
        Categorie categorie = categorieRepository.findById(categoryIdAsLong)
                .orElseThrow(() -> new IllegalArgumentException("Catégorie non trouvée"));

        articleToUpdate.setNomArticle(updatedArticle.getNomArticle());
        articleToUpdate.setDescription(updatedArticle.getDescription());
        articleToUpdate.setDateDebutEncheres(updatedArticle.getDateDebutEncheres());
        articleToUpdate.setDateFinEncheres(updatedArticle.getDateFinEncheres());
        articleToUpdate.setPrixInitial(updatedArticle.getPrixInitial());
        articleToUpdate.setPrixVente(updatedArticle.getPrixVente());

        articleToUpdate.setNoUtilisateur(originalUserId);
        articleToUpdate.setNoCategorie(categorie.getNo_categorie());

        articleRepository.save(articleToUpdate);

        return "redirect:/articles";
    }

    @GetMapping("/encherir/article/{noArticle}")
    public String encheririArticle(@PathVariable("noArticle") Long articleNo, Model model) {
        Article article = articleRepository.findById(articleNo)
                .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec le numéro : " + articleNo));
        List<Categorie> categories = categorieRepository.findAll();

        LocalDateTime dateEnchere = null;
        BigDecimal montantEnchere = null;
        Enchere enchere = enchereRepository.findByNoArticle(articleNo);
        String formattedDateEnchere = "";
        if (enchere != null) {
            dateEnchere = enchere.getDateEnchere();
            montantEnchere = enchere.getMontantEnchere();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            formattedDateEnchere = dateEnchere.format(formatter);
        }


        model.addAttribute("article", article);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryId", article.getNoCategorie());
        model.addAttribute("dateEnchere", formattedDateEnchere);
        model.addAttribute("montantEnchere", montantEnchere);

        return "articleEncherir";
    }




    @PostMapping("/encherir/article/{noArticle}")
    public String encherirArticle(@PathVariable("noArticle") Long articleNo,
                                  @RequestParam("categoryId") String categoryId,
                                  @RequestParam("montantEnchere") BigDecimal nouveauMontantEnchere,
                                  @AuthenticationPrincipal UserDetails currentUser,
                                  Model model) {

            Article article = articleRepository.findById(articleNo)
                    .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec le numéro : " + articleNo));

            Enchere enchereExistante = enchereRepository.findByNoArticle(articleNo);

            if (enchereExistante != null && nouveauMontantEnchere.compareTo(enchereExistante.getMontantEnchere()) <= 0) {

                model.addAttribute("error", "Le montant de l'enchère doit être supérieur à l'enchère actuelle.");

                model.addAttribute("article", article);
                model.addAttribute("categoryId", categoryId);
                model.addAttribute("dateEnchere", enchereExistante.getDateEnchere());
                model.addAttribute("montantEnchere", enchereExistante.getMontantEnchere());
                // Charger les catégories pour l'affichage
                List<Categorie> categories = categorieRepository.findAll();
                model.addAttribute("categories", categories);
                return "articleEncherir";
            }

        Enchere enchere = new Enchere();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByPseudo(username);
        Long userId = user.getNoUtilisateur();
        enchere.setNoUtilisateur(Math.toIntExact(userId));

        enchere.setNoArticle(Math.toIntExact(articleNo));
        enchere.setMontantEnchere(nouveauMontantEnchere);
        enchere.setDateEnchere(LocalDateTime.now());

        enchereRepository.save(enchere);

        return "redirect:/encherir/article/" + articleNo;
    }

    @GetMapping("/articles")
    public String getAllArticles(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "articles";
    }
}
