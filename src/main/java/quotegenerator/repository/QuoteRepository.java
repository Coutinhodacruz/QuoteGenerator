package quotegenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quotegenerator.model.Quote;
import quotegenerator.model.User;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, Long> {


    List<Quote> findByUserGenerated(boolean userGenerated);


}
