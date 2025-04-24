package model.dao;

import model.CurrencyAlreadyExistExeption;
import model.DataBaseIsNotAvalibleExeption;

import java.util.List;
import java.util.Optional;

public interface CurrenciesCRUD<K, T>{

    List<T> findAll() throws DataBaseIsNotAvalibleExeption;

    Optional<T> findById(K id) throws DataBaseIsNotAvalibleExeption;

    void update(T entity);

    Optional<T> save(T entity) throws DataBaseIsNotAvalibleExeption, CurrencyAlreadyExistExeption;
}
