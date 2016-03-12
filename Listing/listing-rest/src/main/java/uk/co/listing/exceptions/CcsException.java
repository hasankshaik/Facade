package uk.co.listing.exceptions;

public class CcsException extends RuntimeException {

    private static final long serialVersionUID = -411616450069839234L;

    public CcsException() {
        super();
    }

    public CcsException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CcsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CcsException(final String message) {
        super(message);
    }

    public CcsException(final Throwable cause) {
        super(cause);
    }

}
