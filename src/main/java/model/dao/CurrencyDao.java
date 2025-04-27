package model.dao;

import model.exceptions.CurrencyDoesNotExistException;
import model.exceptions.DataBaseIsNotAvalibleException;
import model.entity.CurrencyEntity;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyDao implements CurrencyCRUD<String, CurrencyEntity>{

    private static final String FIND_BY_CODE = "SELECT * FROM Currencies WHERE Code=?";
    private static final CurrencyDao INSTANCE = new CurrencyDao();

    private CurrencyDao(){}


    @Override
    public CurrencyEntity findByCode(String code) throws DataBaseIsNotAvalibleException, CurrencyDoesNotExistException {
        try(
                Connection connection = ConnectionManager.get();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_CODE)
        ) {

            statement.setString(1, code);

            ResultSet result = statement.executeQuery();

            if (!result.next())
            {
                throw new CurrencyDoesNotExistException();
            }

            return builder(result);

        } catch (SQLException e) {
            throw new DataBaseIsNotAvalibleException(e);
        }

    }



    private CurrencyEntity builder(ResultSet resultSet) throws SQLException{
        return new CurrencyEntity(
                    resultSet.getLong("ID"),
                    resultSet.getString("Code"),
                    resultSet.getString("FullName"),
                    resultSet.getString("Sign")
        );

    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }
}
