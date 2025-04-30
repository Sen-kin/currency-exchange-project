package exception;

import java.sql.SQLException;

public class DataBaseAccessException extends RuntimeException {
    public DataBaseAccessException(String message) {
        super(message);
    }
}
