package model.dao;

import model.DataBaseIsNotAvalibleExeption;
import model.ExchangeRateAlreadyExistExeption;
import model.ExchangeRateCodeDoesNotExistExeption;
import model.dto.CurrencyDto;
import model.dto.ExchangeRatesDto;
import lombok.SneakyThrows;
import util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private static final String FIND_BY_CODES = """
            SELECT ExchangeRates.ID ExchangeId,
                   C.ID baseId, C.FullName baseName, C.Code baseCode, C.Sign baseSign,
                   C2.ID targetId, C2.FullName targetName, C2.Code targetCode, C2.Sign targetSign,
                   Rate ExchangeRate FROM ExchangeRates
                       JOIN Currencies C on C.ID = ExchangeRates.BaseCurrencyId
                       JOIN Currencies C2 on C2.ID = ExchangeRates.TargetCurrencyId
                    WHERE C.Code =? AND C2.Code=?
            """;

    private static final String CREATE_EXCHANGE_RATE = """
            INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES
                   (
                    (SELECT ID FROM Currencies WHERE Code=?),
                    (SELECT ID FROM Currencies WHERE Code=?),
                    ?
                   );
            """;
    private static final String FIND_BY_ID = "SELECT * FROM ExchangeRates WHERE ID=?";

    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();
    private static final CurrenciesDao CURRENCIES = CurrenciesDao.getInstance();

    private ExchangeRatesDao(){}


    @Override
    public List<ExchangeRatesDto> findAllExchangeRates() throws DataBaseIsNotAvalibleExeption {
        try(var connection = ConnectionManager.get();
            var statement = connection.prepareStatement(FIND_ALL_EXCHANGE_RATES)
        ) {
           var resultSet =  statement.executeQuery();

            List<ExchangeRatesDto> exchangeRates = new ArrayList<>();

            while(resultSet.next()) exchangeRates.add(builder(resultSet));

            return exchangeRates;
        }
        catch (SQLException e) {
            throw new DataBaseIsNotAvalibleExeption(e);
        }
    }

    @Override
    public Optional<ExchangeRatesDto> findExchangeRate(String baseCode, String targetCode) throws DataBaseIsNotAvalibleExeption {
        try(var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(FIND_BY_CODES)) {

            statement.setString(1, baseCode);
            statement.setString(2, targetCode);

        var resultset = statement.executeQuery();
        if (resultset.getLong("ExchangeId") == 0) return Optional.empty();

        return Optional.of(builder(resultset));

        } catch (SQLException e) {
            throw new DataBaseIsNotAvalibleExeption(e);
        }
    }

    @Override
    public Optional<ExchangeRatesDto> createExchangeRate(ExchangeRatesDto exchangeRatesDto) throws DataBaseIsNotAvalibleExeption, ExchangeRateAlreadyExistExeption, ExchangeRateCodeDoesNotExistExeption {
        try(var connection = ConnectionManager.get();
            var statement = connection.prepareStatement(CREATE_EXCHANGE_RATE, Statement.RETURN_GENERATED_KEYS)
        ) {

            if (
                    !CURRENCIES.selectAllCodes().contains(exchangeRatesDto.baseCurrency().code()) ||
                    !CURRENCIES.selectAllCodes().contains(exchangeRatesDto.targetCurrency().code())
            ) throw new ExchangeRateCodeDoesNotExistExeption();


            if (INSTANCE.findExchangeRate(exchangeRatesDto.baseCurrency().code(), exchangeRatesDto.targetCurrency().code()).isPresent())
                throw new ExchangeRateAlreadyExistExeption();

            statement.setString(1, exchangeRatesDto.baseCurrency().code());
            statement.setString(2, exchangeRatesDto.targetCurrency().code());
            statement.setDouble(3, exchangeRatesDto.rate());

            statement.executeUpdate();

            var Id = statement.getGeneratedKeys();

            if (Id.next()) {
              return INSTANCE.findExchangeRateByID(Id.getLong(1));
            }

        } catch (SQLException e) {
            throw new DataBaseIsNotAvalibleExeption(e);
        }
        return Optional.empty();
    }

    private Optional<ExchangeRatesDto> findExchangeRateByID(Long ID) throws DataBaseIsNotAvalibleExeption{
        try (var connection = ConnectionManager.get();
            var statement = connection.prepareStatement(FIND_BY_ID)
        ) {
         statement.setLong(1, ID);

         var result = statement.executeQuery();

         return Optional.of(builder(result));

        } catch (SQLException e) {
            throw new DataBaseIsNotAvalibleExeption(e);
        }
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
}
