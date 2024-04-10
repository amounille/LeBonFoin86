package fr.eni.lebonfoin.controller;



import fr.eni.lebonfoin.entity.Categorie;
import fr.eni.lebonfoin.repository.CategorieRepository;
import fr.eni.lebonfoin.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CategorieController {
    @Autowired
    CategorieRepository categorieRepository;

    private final CategorieService categorieService;

    @GetMapping("/new/categorie")
    public String getCategoriesNew(Model model) {
        Categorie categorie = new Categorie();
        categorie.setLibelle("coucou");
        model.addAttribute("categorie", categorie);
        return "categorieNew";
    }

    @PostMapping("/new/categorie")
    public String saveCategorie(Categorie categorie, Model model) {
        categorieRepository.save(categorie);
        model.addAttribute("categorie", categorie);
        return "categorieNew";
    }

    @Autowired
    public CategorieController(CategorieService categorieService) {

        this.categorieService = categorieService;
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categorieNew";
    }

}
