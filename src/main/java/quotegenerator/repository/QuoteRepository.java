package quotegenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import quotegenerator.model.Quote;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {


    List<Quote> findByUserGenerated(boolean userGenerated);


    boolean existsByContent(String content);


    @Query("SELECT q FROM Quote q WHERE q.userGenerated = ?1 AND q.id < ?2 ORDER BY q.id DESC")
    Long findPreviousQuoteId(boolean userGenerated, Long currentQuoteId);
}
