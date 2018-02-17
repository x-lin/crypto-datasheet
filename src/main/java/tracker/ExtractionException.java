package tracker;

/**
 * @author LinX
 */
public class ExtractionException extends RuntimeException {
    public ExtractionException(final String message) {
        super(message);
    }

    public ExtractionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
