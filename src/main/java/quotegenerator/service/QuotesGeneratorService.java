package quotegenerator.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import quotegenerator.dto.request.JavaMailerRequest;
import quotegenerator.dto.request.LoginRequest;
import quotegenerator.dto.request.RegistrationRequest;
import quotegenerator.dto.response.LoginResponse;
import quotegenerator.dto.response.RegistrationResponse;
import quotegenerator.exception.UserAlreadyExistException;
import quotegenerator.model.Quote;
import quotegenerator.model.User;
import quotegenerator.repository.QuoteRepository;
import quotegenerator.repository.UserRepository;


@Service
@AllArgsConstructor
public class QuotesGeneratorService implements QuotesServices{

    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;

    private final MailService mailService;
    private final TokenService tokenService;

    @Value("${spring.mail.username}")
    private String fromEmail;

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
        javaMailerRequest.setFrom(fromEmail);

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
    public LoginResponse login(LoginRequest loginRequest) {
        return null;
    }
}
