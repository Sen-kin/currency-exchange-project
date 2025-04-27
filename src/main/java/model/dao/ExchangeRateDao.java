package model.dao;

import model.dto.CurrencyDto;
import model.dto.ExchangeRateDto;
import model.exceptions.*;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDao implements ExchangeRateCRUD<String, ExchangeRateDto> {

    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();

    private static final CurrenciesDao CURRENCIES = CurrenciesDao.getInstance();

    private static final String CREATE = """
            INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES
                   (
                    (SELECT ID FROM Currencies WHERE Code=?),
                    (SELECT ID FROM Currencies WHERE Code=?),
                    ?
                   );
            """;

    private static final String FIND_ALL = """
            SELECT ExchangeRates.ID ExchangeId,
                   C.ID baseId, C.FullName baseName, C.Code baseCode, C.Sign baseSign,
                   C2.ID targetId, C2.FullName targetName, C2.Code targetCode, C2.Sign targetSign,
                   Rate ExchangeRate FROM ExchangeRates
                       JOIN Currencies C on C.ID = ExchangeRates.BaseCurrencyId
                       JOIN Currencies C2 on C2.ID = ExchangeRates.TargetCurrencyId""";

    private static final String FIND_BY_ID = """
            SELECT ExchangeRates.ID ExchangeId,
                   C.ID baseId, C.FullName baseName, C.Code baseCode, C.Sign baseSign,
                   C2.ID targetId, C2.FullName targetName, C2.Code targetCode, C2.Sign targetSign,
                   Rate ExchangeRate FROM ExchangeRates
                   JOIN Currencies C on C.ID = ExchangeRates.BaseCurrencyId
                   JOIN Currencies C2 on C2.ID = ExchangeRates.TargetCurrencyId
            WHERE ExchangeRates.ID=?""";

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
    private static final String EXISTS = """
            SELECT ExchangeRates.ID FROM ExchangeRates JOIN Currencies C on C.ID = ExchangeRates.BaseCurrencyId
                       JOIN Currencies C2 on C2.ID = ExchangeRates.TargetCurrencyId
            WHERE C.Code =? AND C2.Code=?
            """;


    private ExchangeRateDao(){}


    @Override
    public List<ExchangeRateDto> findAll() throws DataBaseIsNotAvailableException {
        try(
                Connection connection = ConnectionManager.get();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL)
        ) {
            ResultSet resultSet = statement.executeQuery();

            List<ExchangeRateDto> exchangeRates = new ArrayList<>();

            while(resultSet.next()){
                exchangeRates.add(builder(resultSet));
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new DataBaseIsNotAvailableException(e);
        }
    }

    @Override
    public ExchangeRateDto findByCodes(String baseCode, String targetCode) throws DataBaseIsNotAvailableException, ExchangeRateDoesNotExistException {
        try(
                Connection connection = ConnectionManager.get();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_CODES)
        ) {
            statement.setString(1, baseCode);
            statement.setString(2, targetCode);

            ResultSet resultset = statement.executeQuery();

            if (!resultset.next()){
                throw new ExchangeRateDoesNotExistException();
            }

            return builder(resultset);

        } catch (SQLException e) {
            throw new DataBaseIsNotAvailableException(e);
        }
    }

    @Override
    public ExchangeRateDto create(ExchangeRateDto exchangeRatesDto) throws DataBaseIsNotAvailableException, ExchangeRateAlreadyExistsException, ExchangeRateCodeDoesNotExistException, ExchangeRateCreationException {
        try(
                Connection connection = ConnectionManager.get();
            PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)
        ) {
            if
            (
                    !CURRENCIES.selectAllCodes().contains(exchangeRatesDto.baseCurrency().code()) ||
                    !CURRENCIES.selectAllCodes().contains(exchangeRatesDto.targetCurrency().code())
            )
                throw new ExchangeRateCodeDoesNotExistException();

            if (INSTANCE.existByCodes(exchangeRatesDto.baseCurrency().code(), exchangeRatesDto.targetCurrency().code())) {
                throw new ExchangeRateAlreadyExistsException();
            }

            statement.setString(1, exchangeRatesDto.baseCurrency().code());
            statement.setString(2, exchangeRatesDto.targetCurrency().code());
            statement.setDouble(3, exchangeRatesDto.rate());

            statement.executeUpdate();

            ResultSet Id = statement.getGeneratedKeys();

            return INSTANCE.findById(Id.getLong(1));

        } catch (SQLException e) {
            throw new DataBaseIsNotAvailableException(e);
        } catch (ExchangeRateDoesNotExistException e) {
            throw new ExchangeRateCreationException();
        }
    }

    private boolean existByCodes(String baseCurrencyCode, String targetCurrencyCode) throws DataBaseIsNotAvailableException {
        try (
                Connection connection = ConnectionManager.get();
        PreparedStatement statement = connection.prepareStatement(EXISTS)) {

            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);

            return statement.executeQuery().next();

        } catch (SQLException e) {
            throw new DataBaseIsNotAvailableException(e);
        }
    }

    private ExchangeRateDto findById(Long ID) throws DataBaseIsNotAvailableException, ExchangeRateDoesNotExistException {
        try (
                Connection connection = ConnectionManager.get();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)
        ) {
         statement.setLong(1, ID);

         ResultSet result = statement.executeQuery();

         if (!result.next()){
             throw new ExchangeRateDoesNotExistException();
         }

         return builder(result);

        } catch (SQLException e) {
            throw new DataBaseIsNotAvailableException(e);
        }
    }

    @Override
    public ExchangeRateDto update(ExchangeRateDto exchangeRateDto) throws DataBaseIsNotAvailableException, ExchangeRateDoesNotExistException {

        try(
                Connection connection = ConnectionManager.get();
            PreparedStatement statement = connection.prepareStatement(UPDATE)
        ){

            statement.setDouble(1, exchangeRateDto.rate());
            statement.setString(2, exchangeRateDto.baseCurrency().code());
            statement.setString(3, exchangeRateDto.targetCurrency().code());

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0){
                throw new ExchangeRateDoesNotExistException();
            }

           return INSTANCE.findById(INSTANCE.findByCodes(exchangeRateDto.baseCurrency().code(), exchangeRateDto.targetCurrency().code()).id());

        } catch (SQLException e) {
            throw new DataBaseIsNotAvailableException(e);
        }
    }

    private ExchangeRateDto builder(ResultSet resultSet) throws SQLException {
        return new ExchangeRateDto(
                    resultSet.getLong("ExchangeId"),
                        new CurrencyDto(
                                resultSet.getLong("baseId"),
                                resultSet.getString("baseCode"),
                                resultSet.getString("baseName"),
                                resultSet.getString("baseSign")
                                ),
                        new CurrencyDto(
                                resultSet.getLong("targetId"),
                                resultSet.getString("targetCode"),
                                resultSet.getString("targetName"),
                                resultSet.getString("targetSign")
                        ),
                    resultSet.getDouble("ExchangeRate")
                );
    }

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }
}

