package model.dao;

import model.exceptions.CurrencyAlreadyExistsException;
import model.exceptions.CurrencyCreationException;
import model.exceptions.CurrencyDoesNotExistException;
import model.exceptions.DataBaseIsNotAvailableException;

import java.util.List;

public interface CurrenciesCRUD<K, T>{

    T create(T entity) throws DataBaseIsNotAvailableException, CurrencyAlreadyExistsException, CurrencyCreationException;

    List<T> findAll() throws DataBaseIsNotAvailableException;

    T findById(K id) throws DataBaseIsNotAvailableException, CurrencyDoesNotExistException;

}
