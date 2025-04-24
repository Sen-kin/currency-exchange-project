package model.dao;

import model.DataBaseIsNotAvalibleExeption;
import model.entity.CurrencyEntity;
import util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CurrencyDao implements CurrencyCRUD<String, CurrencyEntity>{

    private static final String FIND_BY_CODE = "SELECT * FROM Currencies WHERE Code=?";
    private static final CurrencyDao INSTANCE = new CurrencyDao();

    private CurrencyDao(){}


    @Override
    public Optional<CurrencyEntity> findByCode(String code) throws DataBaseIsNotAvalibleExeption {
        try(var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(FIND_BY_CODE)){

            statement.setString(1, code);

            var result = statement.executeQuery();

            return Optional.ofNullable(builder(result));
        } catch (SQLException e) {
            throw new DataBaseIsNotAvalibleExeption(e);
        }

    }



    private CurrencyEntity builder(ResultSet resultSet) throws SQLException{

        if (resultSet.getString("Code") == null) return null;

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
