package quotegenerator.service;

import quotegenerator.dto.request.LoginRequest;
import quotegenerator.dto.request.RegistrationRequest;
import quotegenerator.dto.request.UserQuoteRequest;
import quotegenerator.dto.response.LoginResponse;
import quotegenerator.dto.response.RegistrationResponse;
import quotegenerator.dto.response.UserQuoteResponse;
import quotegenerator.model.Quote;

import java.util.List;
import java.util.Optional;

public interface QuotesServices {

    RegistrationResponse register(RegistrationRequest request);

    LoginResponse login(LoginRequest loginRequest);

    UserQuoteResponse userQuote(UserQuoteRequest quoteRequest);


    List<UserQuoteResponse> getAllUserQuotes();

    List<UserQuoteResponse> getUserGeneratedQuotes();
    List<UserQuoteResponse> getNotUserGeneratedQuotes();

    String getKanyeQuote();

    String generateGoQuote();

    String generateGoodReadsQuote();

    Optional<Quote> getPreviousQuote(Long currentQuoteId);
}

