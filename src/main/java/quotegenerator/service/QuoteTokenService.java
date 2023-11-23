package quotegenerator.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import quotegenerator.exception.UserNotFoundException;
import quotegenerator.model.Token;
import quotegenerator.repository.TokenRepository;

import java.util.Random;

@Service
@AllArgsConstructor
public class QuoteTokenService implements TokenService{

    private final TokenRepository tokenRepository;



    @Override
    public String createToken(String userEmail) {
        String token = generateToken();
        Token userToken = new Token();
        userToken.setToken(token);
        userToken.setOwnerEmail(userEmail.toLowerCase());
        Token savedToken = tokenRepository.save(userToken);

          return savedToken.getToken();
    }

    @Override
    public Token findByOwnerEmail(String email) {
        Token token = tokenRepository.findByOwnerEmail(email.toLowerCase())
                .orElseThrow(()-> new UserNotFoundException("The provided email is not attached to the token"));
        return token;
    }

    @Override
    public void deleteToken(Long id) {
        tokenRepository.deleteById(id);
    }

    private String generateToken() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            int digit = random.nextInt(1,9);
            otp.append(digit);
        }
        return String.valueOf(otp);
    }
}
