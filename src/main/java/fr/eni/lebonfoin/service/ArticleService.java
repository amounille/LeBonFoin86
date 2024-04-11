package fr.eni.lebonfoin.service;

import fr.eni.lebonfoin.entity.Article;
import fr.eni.lebonfoin.entity.User;
import fr.eni.lebonfoin.repository.ArticleRepository;
import fr.eni.lebonfoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }


    private boolean isAdmin(String username) {
        User user = userRepository.findByPseudo(username);
        return user != null && user.isAdministrateur();
    }

    public boolean isAdminOrOwner(Authentication authentication,  Long articleNo) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        boolean isAdmin = isAdmin(username);
        Article article = articleRepository.findById(articleNo)
                .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec le numéro : " + articleNo));
        boolean isOwner = article.getNoUtilisateur().equals(userRepository.findByPseudo(username).getNoUtilisateur());

        return isAdmin || isOwner;
    }
}
