package quotegenerator.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quotegenerator.dto.request.LoginRequest;
import quotegenerator.dto.request.RegistrationRequest;
import quotegenerator.dto.response.LoginResponse;
import quotegenerator.dto.response.RegistrationResponse;
import quotegenerator.service.QuotesServices;

@RestController
@RequestMapping("/quote")
@RequiredArgsConstructor
public class QuoteGeneratorController {

    private final QuotesServices quotesServices;


    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest registerRequest){
        var response = quotesServices.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest){
        var response = quotesServices.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
