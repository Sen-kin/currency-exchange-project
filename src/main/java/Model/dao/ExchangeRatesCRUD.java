package Model.dao;


import java.util.List;
import java.util.Optional;

public interface ExchangeRatesCRUD<K, T> {

     List<T> findAllExchangeRates();

     Optional<T> findExchangeRate(K firstID, K secondID);

     void createExchangeRate(T entity);


}
