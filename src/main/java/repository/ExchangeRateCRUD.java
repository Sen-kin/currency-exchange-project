package repository;


import java.util.List;

public interface ExchangeRateCRUD<K, T> {

     List<T> findAll();

     T find(K baseCurrencyCode, K targetCurrencyCode);

     T create(T entity);

     T update(K baseCurrencyCode, K targetCurrencyCode, Double rate);
}
