package Model.dao;


import java.util.List;

public interface ExchangeRatesCRUD<K, T> {

     List<T> findAllExchangeRates();

     List<T> findExchangeRate(K firstID, K secondID);

     void createExchangeRate(T entity);


}
