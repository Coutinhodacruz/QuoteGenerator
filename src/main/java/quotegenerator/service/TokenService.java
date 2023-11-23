package quotegenerator.service;

import quotegenerator.model.Token;

public interface TokenService {

    String createToken(String email);

    Token findByOwnerEmail(String email);

    void deleteToken(Long id);
}
