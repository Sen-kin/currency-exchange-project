package model.dao;

import model.exceptions.CurrencyAlreadyExistsException;
import model.exceptions.CurrencyCreationException;
import model.exceptions.CurrencyDoesNotExistException;
import model.exceptions.DataBaseIsNotAvailableException;
import model.entity.CurrencyEntity;
import util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDao implements CurrenciesCRUD<Long, CurrencyEntity> {

    private static final CurrenciesDao INSTANCE = new CurrenciesDao();

    private static final String CREATE_CURRENCY = "INSERT INTO Currencies(Code, FullName, Sign) VALUES (?, ?, ?)";

    private static final String FIND_ALL = "SELECT * FROM Currencies";

    private static final String FIND_BY_ID = "SELECT * FROM Currencies WHERE ID=?";


    private static final String SELECT_ALL_CODES = "SELECT Code FROM Currencies";

    private CurrenciesDao(){}

    @Override
    public List<CurrencyEntity> findAll() throws DataBaseIsNotAvailableException {
        try(
                Connection connection = ConnectionManager.get();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL)
        ) {
            ResultSet resultSet = statement.executeQuery();

            List<CurrencyEntity> currencies = new ArrayList<>();

            while (resultSet.next()) {
                currencies.add(builder(resultSet));
            }

            return currencies;
        } catch (SQLException e) {
            throw new DataBaseIsNotAvailableException(e);
        }
    }

    @Override
    public CurrencyEntity findById(Long id) throws DataBaseIsNotAvailableException, CurrencyDoesNotExistException {
        try(
                Connection connection = ConnectionManager.get();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)
        ) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.getLong("ID") == 0) {
                throw new CurrencyDoesNotExistException();
            }
                return builder(result);

        } catch (SQLException e) {
            throw new DataBaseIsNotAvailableException(e);
        }
    }

    @Override
    public CurrencyEntity create(CurrencyEntity entity) throws DataBaseIsNotAvailableException, CurrencyAlreadyExistsException, CurrencyCreationException {
        try(
                Connection connection = ConnectionManager.get();
            PreparedStatement statement = connection.prepareStatement(CREATE_CURRENCY, Statement.RETURN_GENERATED_KEYS)
        ) {

            if (entity.getCode().length() != 3 || entity.getSign().length() > 3){
                throw new CurrencyCreationException();
            }

            if(INSTANCE.selectAllCodes().contains(entity.getCode())) {
                throw new CurrencyAlreadyExistsException();
            }

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getSign());

            statement.executeUpdate();

            ResultSet ID = statement.getGeneratedKeys();

            return INSTANCE.findById(ID.getLong(1));

        } catch (SQLException e) {
            throw new DataBaseIsNotAvailableException(e);
        } catch (CurrencyDoesNotExistException e) {
            throw new CurrencyCreationException();
        }
    }

    public List<String> selectAllCodes() throws DataBaseIsNotAvailableException {
        try(Connection connection = ConnectionManager.get();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CODES)
        ) {
            ResultSet codes = statement.executeQuery();

            List<String> currencyCodes = new ArrayList<>();

            while(codes.next()) {
                currencyCodes.add(codes.getString("Code"));
            }

            return currencyCodes;

        } catch (SQLException e) {
            throw new DataBaseIsNotAvailableException(e);
        }
    }

    private CurrencyEntity builder(ResultSet resultSet) throws SQLException {
        return new CurrencyEntity(
                resultSet.getLong("ID"),
                resultSet.getObject("Code", String.class),
                resultSet.getObject("FullName", String.class),
                resultSet.getObject("Sign", String.class)
        );
    }

    public static CurrenciesDao getInstance() {
        return INSTANCE;
    }


}
