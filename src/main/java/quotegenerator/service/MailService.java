package quotegenerator.service;

import quotegenerator.dto.request.JavaMailerRequest;

public interface MailService {


    void send (JavaMailerRequest javaMailerRequest);
}
