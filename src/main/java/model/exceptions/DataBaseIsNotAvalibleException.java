package model.exceptions;

import java.sql.SQLException;

public class DataBaseIsNotAvalibleException extends Exception {
    public DataBaseIsNotAvalibleException(SQLException e) {
    }
}
