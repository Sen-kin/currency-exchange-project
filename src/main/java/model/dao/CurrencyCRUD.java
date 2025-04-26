package model.dao;

import model.exceptions.CurrencyDoesNotExistException;
import model.exceptions.DataBaseIsNotAvalibleException;

public interface CurrencyCRUD<K, T> {

    T findByCode(K code) throws CurrencyDoesNotExistException, DataBaseIsNotAvalibleException;
}
