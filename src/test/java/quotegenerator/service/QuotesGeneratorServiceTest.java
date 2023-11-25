package quotegenerator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import quotegenerator.dto.request.LoginRequest;
import quotegenerator.dto.request.RegistrationRequest;
import quotegenerator.dto.response.LoginResponse;
import quotegenerator.dto.response.RegistrationResponse;
import quotegenerator.exception.UserAlreadyExistException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class QuotesGeneratorServiceTest {

    @Autowired
    QuotesGeneratorService quotesGeneratorService;
    RegistrationRequest registrationRequest;
    @BeforeEach
    void setUp(){
        registrationRequest = new RegistrationRequest();
    }

    @Test
    public void testUserCanRegister(){
//        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("Aiyeola");
        registrationRequest.setMail("coutinhodacruz10@gmail.com");
        registrationRequest.setPassword("123456");
        RegistrationResponse response = quotesGeneratorService.register(registrationRequest);
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isNotNull();

    }

    @Test
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
    public void testUserCanLogin(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Aiyeola");
        loginRequest.setPassword("123456");
        LoginResponse response = quotesGeneratorService.login(loginRequest);
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isNotNull();
    }

}