package quotegenerator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import quotegenerator.dto.request.LoginRequest;
import quotegenerator.dto.request.RegistrationRequest;
import quotegenerator.dto.request.UserQuoteRequest;
import quotegenerator.dto.response.LoginResponse;
import quotegenerator.dto.response.RegistrationResponse;
import quotegenerator.dto.response.UserQuoteResponse;
import quotegenerator.exception.UserAlreadyExistException;
import quotegenerator.exception.UserQuoteAlreadyExistException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class QuotesGeneratorServiceTest {

    @Autowired
    QuotesGeneratorService quotesGeneratorService;
    RegistrationRequest registrationRequest;

    UserQuoteRequest request;

    @BeforeEach
    void setUp(){
        registrationRequest = new RegistrationRequest();
        request = new UserQuoteRequest();
    }

    @Test
    @Order(1)
    public void testUserCanRegister(){
        registrationRequest.setUsername("Aiyeola");
        registrationRequest.setMail("coutinhodacruz10@gmail.com");
        registrationRequest.setPassword("123456");
        RegistrationResponse response = quotesGeneratorService.register(registrationRequest);
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isNotNull();

    }

    @Test
    @Order(2)
    public void testUserAlreadyRegister(){
        try {
            registrationRequest.setUsername("Aiyeola");
            registrationRequest.setMail("coutinhodacruz10@gmail.com");
            registrationRequest.setPassword("123456");
            RegistrationResponse response = quotesGeneratorService.register(registrationRequest);
            assertThat(response).isNotNull();
            assertThat(response.getMessage()).isNotNull();
        }catch (UserAlreadyExistException e){
            System.out.println(e.getMessage());
        }

        assertThrows(UserAlreadyExistException.class, () -> quotesGeneratorService.register(registrationRequest));
    }

    @Test
    @Order(3)
    public void testUserCanLogin(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Aiyeola");
        loginRequest.setPassword("123456");
        LoginResponse response = quotesGeneratorService.login(loginRequest);
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isNotNull();
    }

    @Test
    @Order(4)
    void testUserQuote() {
        request.setContent("When you're at a concert or event, enjoy the moment," +
                " enjoy being there. " +
                "Try leaving your camera in your pocket.");
        request.setUserGenerated(true);

        UserQuoteResponse response = quotesGeneratorService.userQuote(request);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo(request.getContent());
        assertThat(response.isUserGenerated()).isEqualTo(request.isUserGenerated());
    }

    @Test
    @Order(5)
    public void testUserQuoteAlreadyExist(){
        try {

            request.setContent("When you're at a concert or event, enjoy the moment, " +
                    "enjoy being there. " +
                    "Try leaving your camera in your pocket.");
            request.setUserGenerated(true);

            UserQuoteResponse response = quotesGeneratorService.userQuote(request);

            assertThat(response).isNotNull();
            assertThat(response.getContent()).isEqualTo(request.getContent());
            assertThat(response.isUserGenerated()).isEqualTo(request.isUserGenerated());
        }catch (UserQuoteAlreadyExistException e){
            System.out.println(e.getMessage());
        }
        assertThrows(UserQuoteAlreadyExistException.class, ()-> quotesGeneratorService.userQuote(request));
    }


    @Test
    @Order(6)
    public void testGetUserGeneratedQuotes() {

        request.setContent("Rule number 1: Try not to die. Rule number 2: Don't be a dick.");
        request.setUserGenerated(true);

        UserQuoteResponse response = quotesGeneratorService.userQuote(request);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo(request.getContent());
        assertThat(response.isUserGenerated()).isEqualTo(request.isUserGenerated());

        List<UserQuoteResponse> userGeneratedQuotes = quotesGeneratorService.getUserGeneratedQuotes();

        assertThat(userGeneratedQuotes).isNotNull();
        assertThat(userGeneratedQuotes).isNotEmpty();

        assertThat(userGeneratedQuotes).allMatch(UserQuoteResponse::isUserGenerated);
    }

    @Test
    @Order(7)
    public void testGetNotUserGeneratedQuotes() {
        // Assuming you have some not user-generated quotes in the database for testing
        // Add not user-generated quotes to the database before running this test

        List<UserQuoteResponse> notUserGeneratedQuotes = quotesGeneratorService.getNotUserGeneratedQuotes();

        assertThat(notUserGeneratedQuotes).isNotNull();
        assertThat(notUserGeneratedQuotes).isNotEmpty();

        // Add more assertions based on your expected behavior
        // For example, you can check if all quotes have the correct user-generated flag
        assertThat(notUserGeneratedQuotes).noneMatch(UserQuoteResponse::isUserGenerated);
    }


    @Test
    @Order(8)
    public void testGetAllUserQuotes() {
        UserQuoteRequest requestOne = new UserQuoteRequest();
        requestOne.setContent("Good advice is something a man gives when he is too old to set a bad example.");
        requestOne.setUserGenerated(true);
        UserQuoteResponse response = quotesGeneratorService.userQuote(requestOne);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo(requestOne.getContent());
        assertThat(response.isUserGenerated()).isEqualTo(requestOne.isUserGenerated());

        UserQuoteRequest requestTwo = new UserQuoteRequest();
        requestTwo.setContent("If you're going bald, don't comb your hair over your bald patch.");
        requestTwo.setUserGenerated(true);

        var quoteResponse = quotesGeneratorService.userQuote(requestTwo);

        assertThat(quoteResponse).isNotNull();
        assertThat(quoteResponse.getContent()).isEqualTo(requestTwo.getContent());
        assertThat(quoteResponse.isUserGenerated()).isEqualTo(requestTwo.isUserGenerated());


        List<UserQuoteResponse> allQuotes = quotesGeneratorService.getAllUserQuotes();
           assertEquals(4, quotesGeneratorService.getAllUserQuotes().size());
      assertThat(allQuotes).hasSizeBetween(1,10);

    }

    @Test
    public void testGenerateKanyeQuote() {
        String result = quotesGeneratorService.getKanyeQuote();
        assertThat(result).isNotNull();
        System.out.println("Kanye Quote: " + result);
    }

    @Test
    public void testGenerateGoQuote() {
        String result = quotesGeneratorService.generateGoQuote();
        assertThat(result).isNotNull();
        System.out.println("Go Quote: " + result);
    }

    @Test
    public void testGenerateGoodReadsQuote() {
        String result = quotesGeneratorService.generateGoodReadsQuote();
        assertThat(result).isNotNull();
        System.out.println("GoodReads Quote: " + result);
    }

}