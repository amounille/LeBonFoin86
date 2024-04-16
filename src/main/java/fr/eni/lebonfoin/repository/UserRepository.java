package fr.eni.lebonfoin.repository;

import fr.eni.lebonfoin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {



    User findByEmail(String email);

    User findByResetToken(String resetToken);

   User findByPseudo(String pseudo);

}

