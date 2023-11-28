package quotegenerator.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserQuoteResponse {

    private Long id;

    private String message;

    private String content;

    private boolean userGenerated;
}
