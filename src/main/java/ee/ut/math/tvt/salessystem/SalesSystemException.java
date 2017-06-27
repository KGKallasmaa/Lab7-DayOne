package ee.ut.math.tvt.salessystem;

/**
 * Base class for sales system exceptions
 */
public class SalesSystemException extends RuntimeException {

    public SalesSystemException() {
        super();
    }

    public SalesSystemException(String message) {
        super(message);
    }

    public SalesSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
