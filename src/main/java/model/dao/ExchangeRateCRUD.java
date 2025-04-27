package model.dao;


import model.exceptions.*;

import java.util.List;

public interface ExchangeRateCRUD<K, T> {

     List<T> findAll() throws DataBaseIsNotAvailableException;

     T findByCodes(K baseCode, K targetCode) throws DataBaseIsNotAvailableException, ExchangeRateDoesNotExistException;

     T create(T entity) throws DataBaseIsNotAvailableException, ExchangeRateAlreadyExistsException, ExchangeRateCodeDoesNotExistException, ExchangeRateCreationException;

     T update(T entity) throws DataBaseIsNotAvailableException, ExchangeRateCodeDoesNotExistException, ExchangeRateDoesNotExistException;
}
