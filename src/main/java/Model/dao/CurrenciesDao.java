package Model.dao;

import Model.entity.CurrencyEntity;
import lombok.SneakyThrows;
import util.ConnectionManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesDao implements CurrenciesCRUD<Long, CurrencyEntity> {

    private static final CurrenciesDao INSTANCE = new CurrenciesDao();
    private static final String FIND_ALL = "SELECT * FROM Currencies";
    private static final String FIND_BY_ID = "SELECT * FROM Currencies WHERE ID=?";
    private static final String INSERT_CURRENCY = "INSERT INTO Currencies(Code, FullName, Sign) VALUES (?, ?, ?)";
    private static final String UPDATE_CURRENCY =
            """
            UPDATE Currencies
            SET Code=?, FullName=?, Sign=?
            WHERE ID=?
            """;

    private CurrenciesDao(){}

    @Override
    @SneakyThrows
    public List<CurrencyEntity> findAll() {
        var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(FIND_ALL);

            var resultSet = statement.executeQuery();

            List<CurrencyEntity> currencies = new ArrayList<>();
            while(resultSet.next()){
                    currencies.add(builder(resultSet));
            }
            return currencies;
    }

    @Override
    @SneakyThrows
    public Optional<CurrencyEntity> findById(Long id) {
        var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(FIND_BY_ID);

        statement.setLong(1, id);

            var result = statement.executeQuery();

        return Optional.of(builder(result));
    }

    @Override
    @SneakyThrows
    public void update(CurrencyEntity entity) {
            var connection = ConnectionManager.get();
            var statement = connection.prepareStatement(UPDATE_CURRENCY);

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign());
            statement.setLong(4, entity.getId());

            statement.executeUpdate();
    }

    @Override
    @SneakyThrows
    public CurrencyEntity save(CurrencyEntity entity) {
        var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(INSERT_CURRENCY);

        statement.setString(1, entity.getCode());
        statement.setString(2, entity.getFullName());
        statement.setString(3, entity.getSign());

        statement.executeUpdate();

        return entity;
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

    public static CurrenciesDao getInstance() {
        return INSTANCE;
    }


}
