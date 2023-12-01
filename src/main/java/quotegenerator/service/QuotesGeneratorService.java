package quotegenerator.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import quotegenerator.config.MailConfig;
import quotegenerator.dto.request.JavaMailerRequest;
import quotegenerator.dto.request.LoginRequest;
import quotegenerator.dto.request.RegistrationRequest;
import quotegenerator.dto.request.UserQuoteRequest;
import quotegenerator.dto.response.LoginResponse;
import quotegenerator.dto.response.RegistrationResponse;
import quotegenerator.dto.response.UserQuoteResponse;
import quotegenerator.exception.*;
import quotegenerator.model.Quote;
import quotegenerator.model.User;
import quotegenerator.repository.QuoteRepository;
import quotegenerator.repository.UserRepository;
import quotegenerator.utils.JwtUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class QuotesGeneratorService implements QuotesServices{


    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;

    private final MailService mailService;
    private final TokenService tokenService;

    private final WebClient webClient = WebClient.create();
    private MailConfig mailConfig;


    @Override
    public RegistrationResponse register(RegistrationRequest request) {
        String email = request.getMail().toLowerCase().trim();
        String password = request.getPassword();
        String username = request.getUsername();
        if(userAlreadyExist(email)) throw new UserAlreadyExistException(request.getMail() + "User Already Exist");

        User user = userMapper(email,password, username);

        String token = tokenService.createToken(email);

        JavaMailerRequest javaMailerRequest = new JavaMailerRequest();
        javaMailerRequest.setTo(email);
        javaMailerRequest.setMessage("Hello bellow is your token " + token);
        javaMailerRequest.setSubject("Quote verification otp");
        javaMailerRequest.setFrom(mailConfig.getFromEmail());

        sendToken(javaMailerRequest);


        User savedUser = userRepository.save(user);

        RegistrationResponse response = new RegistrationResponse();
        response.setId(savedUser.getId());
        response.setMessage("Registration Successful");
        return response;
    }

    private User userMapper(String email, String password, String username) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        user.setEnabled(true);
        return user;
    }


    private void sendToken(JavaMailerRequest javaMailerRequest){
        mailService.send(javaMailerRequest);
    }

    private boolean userAlreadyExist(String mail) {
        return userRepository.findByEmail(mail).isPresent();
    }


    @Override
    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        log.info("User name -->{}", username);
        log.info("User password -->{}", password);
        return verifyLoginDetails(username, password);
    }

    private LoginResponse verifyLoginDetails(String username, String password) {
        log.info("Attempting to log in user with username: {}", username);

        User user = userRepository.findByUsername(username);

        if (user != null) {
            log.info("User found in the database: {}", user);

            if (user.getPassword().equals(password)) {
                log.info("Password matches. Login successful for user: {}", username);
                LoginResponse loginResponse = loginResponseMapper(user);
                loginResponse.setMessage("Login Successful");
                return loginResponse;
            } else {
                log.warn("Password does not match for user: {}", username);
                throw new UserLoginWithInvalidCredentialsException("Invalid Login Details");
            }
        } else {
            log.warn("User not found in the database for username: {}", username);
            throw new UserLoginWithInvalidCredentialsException("Invalid Login Details");
        }
    }


    private static LoginResponse loginResponseMapper(User user) {
        LoginResponse loginResponse = new LoginResponse();
        String accessToken = JwtUtils.generateAccessToken(user.getId());
        BeanUtils.copyProperties(user, loginResponse);
        loginResponse.setJwtToken(accessToken);
        log.info("token --> {}", accessToken);
        loginResponse.setMessage("Login Successful");
        return loginResponse;
    }


//    @Override
//    public LoginResponse login(LoginRequest loginRequest) {
//        String username = loginRequest.getUsername();
//        String password = loginRequest.getPassword();
//
//        boolean isAuthenticated = authenticate(username, password);
//        if (!isAuthenticated) {
//            throw new UserLoginWithInvalidCredentialsException("Invalid credentials");
//        }
//
//        LoginResponse loginResponse = new LoginResponse();
//        loginResponse.setMessage("Login successful");
//        return loginResponse;
//    }

    @Override
    public UserQuoteResponse userQuote(UserQuoteRequest quoteRequest) {
        Quote userQuote = new Quote();
        userQuote.setContent(quoteRequest.getContent());
        userQuote.setUserGenerated(quoteRequest.isUserGenerated());

        if (quoteAlreadyExist(quoteRequest.getContent())) {
            throw new UserQuoteAlreadyExistException("Quote Already Exist");
        }

        quoteRepository.save(userQuote);

        return mapToResponse(userQuote);
    }

    private boolean quoteAlreadyExist(String content) {
        return quoteRepository.existsByContent(content);
    }

    private UserQuoteResponse mapToResponse(Quote quote) {
        UserQuoteResponse response = new UserQuoteResponse();
        response.setContent(quote.getContent());
        response.setUserGenerated(quote.isUserGenerated());
        response.setMessage("Quote added successfully");
        return response;
    }

    @Override
    public List<UserQuoteResponse> getAllUserQuotes() {
        List<Quote> allQuotes = quoteRepository.findAll();
        return allQuotes.stream()
                .map(this::mapToUserQuoteResponse)
                .collect(Collectors.toList());
    }

    private UserQuoteResponse mapToUserQuoteResponse(Quote userQuote) {
        UserQuoteResponse response = new UserQuoteResponse();
        response.setId(userQuote.getId());
        response.setContent(userQuote.getContent());
        response.setUserGenerated(userQuote.isUserGenerated());
        return response;
    }

    @Override
    public List<UserQuoteResponse> getUserGeneratedQuotes() {
        List<Quote> userGeneratedQuotes = quoteRepository.findByUserGenerated(true);
        return mapToResponseList(userGeneratedQuotes);
    }

    @Override
    public List<UserQuoteResponse> getNotUserGeneratedQuotes() {
        List<Quote> notUserGeneratedQuotes = quoteRepository.findByUserGenerated(false);
        return mapToResponseList(notUserGeneratedQuotes);
    }


    private List<UserQuoteResponse> mapToResponseList(List<Quote> quotes) {
        return quotes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public String  getKanyeQuote(){
        String kanyeQuote = webClient.get()
                .uri("https://api.kanye.rest")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (kanyeQuote != null) {
            Quote quote = new Quote();
            quote.setContent(kanyeQuote);
            quote.setUserGenerated(true);

            quoteRepository.save(quote);
        }
        return kanyeQuote;
    }


    @Override
    public String generateGoQuote() {
        String goQuote = webClient.get()
                .uri("https://api.adviceslip.com/advice")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (goQuote != null){
            Quote quote = new Quote();
            quote.setContent(goQuote);
            quote.setUserGenerated(true);

            quoteRepository.save(quote);
        }
        return goQuote;
    }

    @Override
    public String generateGoodReadsQuote() {
        return webClient.get()
                .uri("https://www.goodreads.com/quotes/")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public Optional<Quote> getPreviousQuote(Long currentQuoteId) {
        Quote currentQuote = quoteRepository.findById(currentQuoteId).orElse(null);

        if (currentQuote == null) {
            return Optional.empty();
        }

        boolean isUserGenerated = currentQuote.isUserGenerated();
        Long previousQuoteId = quoteRepository.findPreviousQuoteId(isUserGenerated, currentQuoteId);

        if (previousQuoteId != null) {
            return quoteRepository.findById(previousQuoteId);
        } else {
            return Optional.empty();
        }
    }


    private boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null){
            return user.getPassword().equals(password);
        }
        return false;
    }


}





































//
//
//    @Override
//    public UserQuoteResponse getNextQuote(boolean userGenerated) {
//        Long nextQuoteId = getNextQuoteId(userGenerated);
//        Quote nextQuote = quoteRepository.findById(nextQuoteId)
//                .orElseThrow(() -> new NextQuoteNotFoundException("Next quote not found"));
//
//        updateCurrentQuoteId(nextQuoteId);
//        return mapToUserQuoteResponse(nextQuote);
//    }
//
//    private void updateCurrentQuoteId(Long nextQuoteId) {
//        currentQuoteId = nextQuoteId;
//    }
//
//    @Override
//    public UserQuoteResponse getPreviousQuote(boolean userGenerated) {
//        Long previousQuoteId = getPreviousQuoteId(userGenerated);
//        Quote previousQuote = quoteRepository.findById(previousQuoteId)
//                .orElseThrow(() -> new PreviousQuoteNotFoundException("Previous quote not found"));
//
//        updateCurrentQuoteId(previousQuoteId);
//        return mapToUserQuoteResponse(previousQuote);
//    }
//
//    private Long getNextQuoteId(boolean userGenerated) {
//        Long currentQuoteId = getCurrentQuoteId();
//        return quoteRepository.findNextQuote(userGenerated, currentQuoteId);
//    }
//
//    private Long getPreviousQuoteId(boolean userGenerated) {
//        Long currentQuoteId = getCurrentQuoteId();
//        return quoteRepository.findPreviousQuote(userGenerated, currentQuoteId);
//    }

//    private Long getCurrentQuoteId() {
//        return currentQuoteId;
//    }
