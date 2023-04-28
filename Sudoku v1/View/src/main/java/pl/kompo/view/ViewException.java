package pl.kompo.view;

public class ViewException extends RuntimeException {
    public ViewException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewException(String message) {
        super(message);
    }

    public ViewException() {
        super();
    }
}
