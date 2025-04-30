package repository;

import com.zaxxer.hikari.HikariDataSource;
import exceptions.CurrencyAlreadyExistsException;
import exceptions.DataBaseAccessException;
import models.entity.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository implements CurrencyCRUD<String, Currency> {

    private final HikariDataSource dataSource;
    private static final String CREATE_CURRENCY = "INSERT INTO Currencies(Code, FullName, Sign) VALUES (?, ?, ?)";
    private static final String FIND_ALL = "SELECT ID, Code, FullName, Sign FROM Currencies";
    private static final String FIND_BY_CODE = "SELECT ID, Code, FullName, Sign FROM Currencies WHERE Code=?";

    public CurrencyRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Currency> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = statement.executeQuery();
            List<models.entity.Currency> currencies = new ArrayList<>();

            while (resultSet.next()) {
                currencies.add(builder(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new DataBaseAccessException("Server error");
        }
    }

    @Override
    public Currency find(String code) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CODE)) {

            statement.setString(1, code);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                return null;
            }
            return builder(result);
        } catch (SQLException e) {
            throw new DataBaseAccessException("Server error");
        }
    }

    @Override
    public models.entity.Currency create(models.entity.Currency currency) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_CURRENCY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSign());

            statement.executeUpdate();
            Long id = statement.getGeneratedKeys().getLong(1);

            return new models.entity.Currency(id, currency.getCode(), currency.getName(), currency.getSign());
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) { // UNIQUE INDEX
                throw new CurrencyAlreadyExistsException("Currency with this code already exists");
            }
            throw new DataBaseAccessException("Server error");
        }
    }

    private models.entity.Currency builder(ResultSet resultSet) throws SQLException {
        return new models.entity.Currency(
                resultSet.getLong("ID"),
                resultSet.getString("Code"),
                resultSet.getString("FullName"),
                resultSet.getString("Sign")
        );
    }
}
