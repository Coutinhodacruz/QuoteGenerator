package quotegenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quotegenerator.model.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByOwnerEmail(String email);

}