package model.exceptions;

import java.sql.SQLException;

public class DataBaseIsNotAvailableException extends Exception {
    public DataBaseIsNotAvailableException(SQLException e) {
    }
}
