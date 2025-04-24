package model.dao;


import model.DataBaseIsNotAvalibleExeption;

import java.util.List;
import java.util.Optional;

public interface ExchangeRatesCRUD<K, T> {

     List<T> findAllExchangeRates() throws DataBaseIsNotAvalibleExeption;

     Optional<T> findExchangeRate(K firstID, K secondID) throws DataBaseIsNotAvalibleExeption;

     void createExchangeRate(T entity);


}
