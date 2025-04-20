package Model.dao;

import Model.entity.CurrencyEntity;
import lombok.SneakyThrows;
import util.ConnectionManager;

import java.sql.ResultSet;
import java.util.Optional;

public class CurrencyDao implements CurrencyCRUD<String, CurrencyEntity>{

    private static final String FIND_BY_CODE = "SELECT * FROM Currencies WHERE Code=?";
    private static final CurrencyDao INSTANCE = new CurrencyDao();

    private CurrencyDao(){}


    @Override
    @SneakyThrows
    public Optional<CurrencyEntity> findByCode(String code) {
        var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(FIND_BY_CODE);

        statement.setString(1, code);

        var result = statement.executeQuery();

        return Optional.of(builder(result));
    }


    @SneakyThrows
    private CurrencyEntity builder(ResultSet resultSet){
        return new CurrencyEntity(
                resultSet.getLong("ID"),
                resultSet.getObject("Code", String.class),
                resultSet.getObject("FullName", String.class),
                resultSet.getObject("Sign", String.class)
        );
    }


    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

}
