package quotegenerator.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JavaMailerRequest {

    private String to;
    private String subject;
    private String message;
    private String from = "dominicrotimi@gmail.com";
}
