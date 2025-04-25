package model.dao;


import model.DataBaseIsNotAvalibleExeption;
import model.ExchangeRateAlreadyExistExeption;
import model.ExchangeRateCodeDoesNotExistExeption;
import model.dto.ExchangeRatesDto;

import java.util.List;
import java.util.Optional;

public interface ExchangeRatesCRUD<K, T> {

     List<T> findAllExchangeRates() throws DataBaseIsNotAvalibleExeption;

     Optional<T> findExchangeRate(K firstID, K secondID) throws DataBaseIsNotAvalibleExeption;

     Optional<ExchangeRatesDto> createExchangeRate(T entity) throws DataBaseIsNotAvalibleExeption, ExchangeRateAlreadyExistExeption, ExchangeRateCodeDoesNotExistExeption;


}
