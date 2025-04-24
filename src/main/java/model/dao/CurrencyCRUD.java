package model.dao;

import model.DataBaseIsNotAvailibleExeption;
import model.InvalidCodeExeption;

import java.util.Optional;

public interface CurrencyCRUD<K, T> {

    Optional<T> findByCode(K code) throws InvalidCodeExeption, DataBaseIsNotAvailibleExeption;
}
