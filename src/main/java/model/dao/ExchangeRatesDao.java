package model.dao;

import model.DataBaseIsNotAvailibleExeption;
import model.dto.CurrencyDto;
import model.dto.ExchangeRatesDto;
import lombok.SneakyThrows;
import util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao implements ExchangeRatesCRUD<String, ExchangeRatesDto> {

    private static final String FIND_ALL_EXCHANGE_RATES = """
            SELECT ExchangeRates.ID ExchangeId,
                   C.ID baseId, C.FullName baseName, C.Code baseCode, C.Sign baseSign,
                   C2.ID targetId, C2.FullName targetName, C2.Code targetCode, C2.Sign targetSign,
                   Rate ExchangeRate FROM ExchangeRates
                       JOIN Currencies C on C.ID = ExchangeRates.BaseCurrencyId
                       JOIN Currencies C2 on C2.ID = ExchangeRates.TargetCurrencyId""";
    private static final String FIND_EXCHANGE_RATE_BY_CODES = """
            SELECT ExchangeRates.ID ExchangeId,
                   C.ID baseId, C.FullName baseName, C.Code baseCode, C.Sign baseSign,
                   C2.ID targetId, C2.FullName targetName, C2.Code targetCode, C2.Sign targetSign,
                   Rate ExchangeRate FROM ExchangeRates
                       JOIN Currencies C on C.ID = ExchangeRates.BaseCurrencyId
                       JOIN Currencies C2 on C2.ID = ExchangeRates.TargetCurrencyId
                    WHERE C.Code =? AND C2.Code=?
            """;
    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();

    private ExchangeRatesDao(){}


    @Override
    public List<ExchangeRatesDto> findAllExchangeRates() throws DataBaseIsNotAvailibleExeption{

        try(var connection = ConnectionManager.get();
            var statement = connection.prepareStatement(FIND_ALL_EXCHANGE_RATES)
        ) {
           var resultSet =  statement.executeQuery();

            List<ExchangeRatesDto> exchangeRates = new ArrayList<>();

            while(resultSet.next()) exchangeRates.add(builder(resultSet));

            return exchangeRates;
        }
        catch (SQLException e) {
            throw new DataBaseIsNotAvailibleExeption(e);
        }
    }

    @Override
    public Optional<ExchangeRatesDto> findExchangeRate(String baseCode, String targetCode) throws DataBaseIsNotAvailibleExeption{
        try(var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(FIND_EXCHANGE_RATE_BY_CODES)) {

            statement.setString(1, baseCode);
            statement.setString(2, targetCode);

        var resultset = statement.executeQuery();
        if (resultset.getLong("ExchangeId") == 0) return Optional.empty();

        return Optional.of(builder(resultset));

        } catch (SQLException e) {
            throw new DataBaseIsNotAvailibleExeption(e);
        }
    }

    @Override
    public void createExchangeRate(ExchangeRatesDto entity) {

    }

    @SneakyThrows
    private ExchangeRatesDto builder(ResultSet resultSet){
        return new ExchangeRatesDto(
                resultSet.getLong("ExchangeId"),
                new CurrencyDto(
                        resultSet.getLong("baseId"),
                        resultSet.getString("baseName"),
                        resultSet.getString("baseCode"),
                        resultSet.getString("baseSign")
                        ),
                new CurrencyDto(
                        resultSet.getLong("targetId"),
                        resultSet.getString("targetName"),
                        resultSet.getString("targetCode"),
                        resultSet.getString("targetSign")
                ),
                resultSet.getDouble("ExchangeRate")
                );
    }


    public static ExchangeRatesDao getInstance() {
        return INSTANCE;

    }


    public static void main(String[] args) {
        try {
            System.out.println(INSTANCE.findExchangeRate("USD", "EUR"));
        } catch (DataBaseIsNotAvailibleExeption e) {
            throw new RuntimeException(e);
        }
    }
}
