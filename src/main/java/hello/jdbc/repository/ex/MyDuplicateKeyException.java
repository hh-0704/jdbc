package hello.jdbc.repository.ex;

public class MyDuplicateKeyException extends MyDbException {
    public MyDuplicateKeyException(String message) {
        super(message);
    }

    public MyDuplicateKeyException() {
    }

    public MyDuplicateKeyException(Throwable cause) {
        super(cause);
    }

    public MyDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
