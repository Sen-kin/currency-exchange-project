package model.dao;

import model.exceptions.CurrencyDoesNotExistException;
import model.exceptions.DataBaseIsNotAvailableException;

public interface CurrencyCRUD<K, T> {

    T findByCode(K code) throws CurrencyDoesNotExistException, DataBaseIsNotAvailableException;
}
