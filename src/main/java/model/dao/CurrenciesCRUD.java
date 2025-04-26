package model.dao;

import model.exceptions.CurrencyAlreadyExistsException;
import model.exceptions.CurrencyCreationException;
import model.exceptions.CurrencyDoesNotExistException;
import model.exceptions.DataBaseIsNotAvalibleException;

import java.util.List;

public interface CurrenciesCRUD<K, T>{

    T create(T entity) throws DataBaseIsNotAvalibleException, CurrencyAlreadyExistsException, CurrencyCreationException;

    List<T> findAll() throws DataBaseIsNotAvalibleException;

    T findById(K id) throws DataBaseIsNotAvalibleException, CurrencyDoesNotExistException;

}
