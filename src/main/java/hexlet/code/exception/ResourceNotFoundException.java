package hexlet.code.exception;

/**
 * Thrown when a requested resource does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates exception with message.
     * @param message error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
