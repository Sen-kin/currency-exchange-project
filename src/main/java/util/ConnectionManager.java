package util;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {


    static{
        loadDriver();
    }

    @SneakyThrows
    private static void loadDriver() {
        Class.forName("org.sqlite.JDBC");
    }

    @SneakyThrows
        public static Connection get(){
            return DriverManager.getConnection("jdbc:sqlite:C:\\Users\\User\\IdeaProjects\\currency-exchange\\identifier.sqlite");

    }
}
