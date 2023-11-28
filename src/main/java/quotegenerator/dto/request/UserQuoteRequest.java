package quotegenerator.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserQuoteRequest {

    private String content;

    private boolean userGenerated;
}
