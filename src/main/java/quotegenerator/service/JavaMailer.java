package quotegenerator.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import quotegenerator.dto.request.JavaMailerRequest;

@Service
@AllArgsConstructor
@Configuration
public class JavaMailer implements MailService{
    private final JavaMailSender mailSender;

    @Override
    public void send(JavaMailerRequest javaMailerRequest) {
        String mailReceiver = javaMailerRequest.getTo();
        String  message = javaMailerRequest.getMessage();
        String subject = javaMailerRequest.getSubject();
        String sender = javaMailerRequest.getFrom();
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(mailReceiver);
            simpleMailMessage.setText(message);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setFrom(sender);

            mailSender.send(simpleMailMessage);
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }

    }
}