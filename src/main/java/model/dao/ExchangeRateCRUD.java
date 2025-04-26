package model.dao;


import model.exceptions.*;

import java.util.List;

public interface ExchangeRateCRUD<K, T> {

     List<T> findAll() throws DataBaseIsNotAvalibleException;

     T findByCodes(K baseCode, K targetCode) throws DataBaseIsNotAvalibleException, ExchangeRateDoesNotExistException;

     T create(T entity) throws DataBaseIsNotAvalibleException, ExchangeRateAlreadyExistsException, ExchangeRateCodeDoesNotExistException, ExchangeRateCreationException;

     T update(T entity) throws DataBaseIsNotAvalibleException, ExchangeRateCodeDoesNotExistException, ExchangeRateDoesNotExistException;
}
