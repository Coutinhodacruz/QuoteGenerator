package quotegenerator.exception;

public class PreviousQuoteNotFoundException extends RuntimeException {
    public PreviousQuoteNotFoundException(String message) {
        super(message);
    }
}
