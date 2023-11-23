package quotegenerator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import quotegenerator.dto.request.RegistrationRequest;
import quotegenerator.dto.response.RegistrationResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuotesGeneratorServiceTest {

    @Autowired
    QuotesGeneratorService quotesGeneratorService;

    @Test
    public void testUserCanRegister(){
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("Aiyeola");
        registrationRequest.setMail("coutinhodacruz10@gmail.com");
        registrationRequest.setPassword("123456");
        RegistrationResponse response = quotesGeneratorService.register(registrationRequest);
        assertThat(response).isNotNull();


    }

}