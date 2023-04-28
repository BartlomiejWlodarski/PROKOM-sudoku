package pl.kompo.model.exceptions;


public class DbException extends DaoException {
    public DbException(Throwable cause) {
        super(cause);
    }

    public DbException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public DbException(String message) {
        super(message);
    }
}
