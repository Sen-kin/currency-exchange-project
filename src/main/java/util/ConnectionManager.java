package util;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {


    static{
        loadDriver();
    }

    @SneakyThrows
    private static void loadDriver() {
        Class.forName("org.sqlite.JDBC");
    }

        public static Connection get(){
            try {
                return DriverManager.getConnection("jdbc:sqlite:C:\\Users\\User\\IdeaProjects\\currency-exchange\\identifier.sqlite");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
}
