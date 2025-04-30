package repository;

import com.zaxxer.hikari.HikariDataSource;
import exceptions.*;
import models.entity.Currency;
import models.entity.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateRepository implements ExchangeRateCRUD<String, ExchangeRate> {

    private final HikariDataSource dataSource;


    private static final String FIND_ALL = """
            SELECT ExchangeRates.ID ExchangeId,
                   C.ID baseId, C.FullName baseName, C.Code baseCode, C.Sign baseSign,
                   C2.ID targetId, C2.FullName targetName, C2.Code targetCode, C2.Sign targetSign,
                   Rate ExchangeRate FROM ExchangeRates
                       JOIN Currencies C on C.ID = ExchangeRates.BaseCurrencyId
                       JOIN Currencies C2 on C2.ID = ExchangeRates.TargetCurrencyId""";
    private static final String FIND_BY_CODES = """
            SELECT ExchangeRates.ID ExchangeId,
                   C.ID baseId, C.FullName baseName, C.Code baseCode, C.Sign baseSign,
                   C2.ID targetId, C2.FullName targetName, C2.Code targetCode, C2.Sign targetSign,
                   Rate ExchangeRate FROM ExchangeRates
                       JOIN Currencies C on C.ID = ExchangeRates.BaseCurrencyId
                       JOIN Currencies C2 on C2.ID = ExchangeRates.TargetCurrencyId
            WHERE C.Code =? AND C2.Code=?
            """;
    private static final String UPDATE = """
            UPDATE ExchangeRates
            SET Rate=?
            WHERE
            BaseCurrencyId=(SELECT ID FROM Currencies WHERE Code=?)
            AND
            TargetCurrencyId = (SELECT ID FROM Currencies WHERE Code=?)
            """;
    private static final String CREATE = """
            INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES
                   (
                    (SELECT ID FROM Currencies WHERE Code=?),
                    (SELECT ID FROM Currencies WHERE Code=?),
                    ?
                   );
            """;

    public ExchangeRateRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<ExchangeRate> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            List<ExchangeRate> exchangeRates = new ArrayList<>();

            while (resultSet.next()) {
                exchangeRates.add(builder(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DataBaseAccessException("Server error");
        }
    }

    @Override
    public ExchangeRate find(String baseCurrencyCode, String targetCurrencyCode) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CODES)) {

            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);

            ResultSet resultset = statement.executeQuery();

            if (!resultset.next()) {
                return null;
            }

            return builder(resultset);
        } catch (SQLException e) {
            throw new DataBaseAccessException("Server error");
        }
    }

    @Override
    public ExchangeRate create(ExchangeRate exchangeRate) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {

            String baseCurrencyCode = exchangeRate.getBaseCurrency().getCode();
            String targetCurrencyCode = exchangeRate.getTargetCurrency().getCode();

            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);
            statement.setDouble(3, exchangeRate.getRate());

            statement.executeUpdate();

            Long id = statement.getGeneratedKeys().getLong(1);

            return new ExchangeRate(id, exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate());

        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new ExchangeRateAlreadyExistsException("Such Currency pair already exist");
            }
            throw new DataBaseAccessException("Server error");
        }
    }

    @Override
    public ExchangeRate update(String baseCurrencyCode, String targetCurrencyCode, Double rate) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            statement.setDouble(1, rate);
            statement.setString(2, baseCurrencyCode);
            statement.setString(3, targetCurrencyCode);

            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                throw new ExchangeRateDoesNotExistException("Exchange rate not found");
            }

            return find(baseCurrencyCode, targetCurrencyCode);
        } catch (SQLException e) {
            throw new DataBaseAccessException("Server error");
        }
    }

    private ExchangeRate builder(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getLong("ExchangeId"),
                new Currency(
                        resultSet.getLong("baseId"),
                        resultSet.getString("baseCode"),
                        resultSet.getString("baseName"),
                        resultSet.getString("baseSign")
                ),
                new Currency(
                        resultSet.getLong("targetId"),
                        resultSet.getString("targetCode"),
                        resultSet.getString("targetName"),
                        resultSet.getString("targetSign")
                ),
                resultSet.getDouble("ExchangeRate")
        );
    }
}

