package model.dao;

import model.CurrencyAlreadyExistExeption;
import model.DataBaseIsNotAvailibleExeption;

import java.util.List;
import java.util.Optional;

public interface CurrenciesCRUD<K, T>{

    List<T> findAll() throws DataBaseIsNotAvailibleExeption;

    Optional<T> findById(K id);

    void update(T entity);

    Optional<T> save(T entity) throws DataBaseIsNotAvailibleExeption, CurrencyAlreadyExistExeption;
}
