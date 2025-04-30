package exception;

public class ExchangeRateDoesNotExistException extends RuntimeException {
    public ExchangeRateDoesNotExistException(String message) {
        super(message);
    }
}
