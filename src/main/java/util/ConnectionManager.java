package util;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;


import java.sql.Connection;
import java.sql.SQLException;

@UtilityClass
public class ConnectionManager {

        private static final HikariConfig config = new HikariConfig();
        private static final HikariDataSource dataSource;
        private static final String URL = "db.url";

        static {
            config.setJdbcUrl(PropertiesUtil.get(URL));
            config.setDriverClassName("org.sqlite.JDBC");
            config.setMaximumPoolSize(5);
            config.setConnectionTimeout(5000);
            config.setIdleTimeout(60000);
            config.setMaxLifetime(1800000);
            config.setMinimumIdle(5);
            dataSource = new HikariDataSource(config);
        }

        public static Connection get() throws SQLException{
                return dataSource.getConnection();
        }
}
