package model;

import java.sql.SQLException;

public class DataBaseIsNotAvalibleException extends Throwable {
    public DataBaseIsNotAvalibleException(SQLException e) {
    }
}
