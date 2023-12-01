package quotegenerator.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quotegenerator.dto.request.LoginRequest;
import quotegenerator.dto.request.RegistrationRequest;
import quotegenerator.dto.request.UserQuoteRequest;
import quotegenerator.dto.response.LoginResponse;
import quotegenerator.dto.response.RegistrationResponse;
import quotegenerator.dto.response.UserQuoteResponse;
import quotegenerator.model.Quote;
import quotegenerator.service.QuotesServices;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/quote")
@RequiredArgsConstructor
public class QuoteGeneratorController {

    private final QuotesServices quotesServices;


    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest registerRequest){
        var response = quotesServices.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest){
        var response = quotesServices.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/userQuote")
    public ResponseEntity<UserQuoteResponse> userQuote(@RequestBody UserQuoteRequest quoteRequest) {
        UserQuoteResponse response = quotesServices.userQuote(quoteRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allUserQuotes")
    public ResponseEntity<List<UserQuoteResponse>> getAllUserQuotes() {
        List<UserQuoteResponse> response = quotesServices.getAllUserQuotes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/userGeneratedQuotes")
    public ResponseEntity<List<UserQuoteResponse>> getUserGeneratedQuotes() {
        List<UserQuoteResponse> response = quotesServices.getUserGeneratedQuotes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notUserGeneratedQuotes")
    public ResponseEntity<List<UserQuoteResponse>> getNotUserGeneratedQuotes() {
        List<UserQuoteResponse> response = quotesServices.getNotUserGeneratedQuotes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/kanyeQuote")
    public ResponseEntity<String> getKanyeQuote() {
        String response = quotesServices.getKanyeQuote();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generateGoQuote")
    public ResponseEntity<String> generateGoQuote() {
        String response = quotesServices.generateGoQuote();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generateGoodReadsQuote")
    public ResponseEntity<String> generateGoodReadsQuote() {
        String response = quotesServices.generateGoodReadsQuote();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getPreviousQuote/{currentQuoteId}")
    public ResponseEntity<Optional<Quote>> getPreviousQuote(@PathVariable Long currentQuoteId) {
        Optional<Quote> response = quotesServices.getPreviousQuote(currentQuoteId);
        return ResponseEntity.ok(response);
    }

}
