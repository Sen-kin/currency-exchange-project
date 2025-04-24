package model.dao;


import model.DataBaseIsNotAvailibleExeption;

import java.util.List;
import java.util.Optional;

public interface ExchangeRatesCRUD<K, T> {

     List<T> findAllExchangeRates() throws DataBaseIsNotAvailibleExeption;

     Optional<T> findExchangeRate(K firstID, K secondID) throws DataBaseIsNotAvailibleExeption;

     void createExchangeRate(T entity);


}
