package hexlet.code.exception;

/**
 * Thrown when a resource cannot be modified because of associations.
 */
public class ResourceAssociatedException extends RuntimeException {

    /**
     * Creates exception with message.
     * @param message error message
     */
    public ResourceAssociatedException(String message) {
        super(message);
    }
}
