package exception;

public class CurrencyDoesNotExistException extends RuntimeException {
    public CurrencyDoesNotExistException(String message) {
        super(message);
    }
}
