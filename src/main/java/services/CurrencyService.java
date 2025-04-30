package services;

import exceptions.CurrencyDoesNotExistException;
import models.dto.CurrencyDTO;
import repository.CurrencyRepository;
import models.entity.Currency;
import mappers.CurrencyMapper;

import java.util.List;

public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public CurrencyService(CurrencyRepository currenciesDao) {
        this.currencyRepository = currenciesDao;
        this.currencyMapper = CurrencyMapper.INSTANCE;
    }

    public List<CurrencyDTO> findAll() {
        List<Currency> currencies = currencyRepository.findAll();

        return currencyMapper.toCurrencyDTOList(currencies);
    }

    public CurrencyDTO find(String code) {
        Currency currency = currencyRepository.find(code);

        if (currency == null) {
            throw new CurrencyDoesNotExistException("Currency not found");
        }

        return currencyMapper.toCurrencyDTO(currency);
    }

    public CurrencyDTO create(CurrencyDTO currencyDTO) {
        Currency currency = currencyMapper.toCurrency(currencyDTO);
        Currency savedCurrency = currencyRepository.create(currency);

        return currencyMapper.toCurrencyDTO(savedCurrency);
    }
}
