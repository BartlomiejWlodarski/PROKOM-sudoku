package pl.kompo.model.exceptions;

public class NameInDbException extends DaoException {
    public NameInDbException(String message) {
        super(message);
    }

    public NameInDbException(Throwable cause) {
        super(cause);
    }
}
