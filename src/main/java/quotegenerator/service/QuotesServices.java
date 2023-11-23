package quotegenerator.service;

import quotegenerator.dto.request.LoginRequest;
import quotegenerator.dto.request.RegistrationRequest;
import quotegenerator.dto.response.LoginResponse;
import quotegenerator.dto.response.RegistrationResponse;

public interface QuotesServices {

    RegistrationResponse register(RegistrationRequest request);

    LoginResponse login(LoginRequest loginRequest);


}
