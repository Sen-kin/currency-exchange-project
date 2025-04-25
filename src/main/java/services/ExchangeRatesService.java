package services;

import model.DataBaseIsNotAvalibleExeption;
import model.ExchangeRateAlreadyExistExeption;
import model.ExchangeRateCodeDoesNotExistExeption;
import model.ExchangeRateIsNotExistExeption;
import model.dao.ExchangeRatesDao;
import model.dto.CurrencyDto;
import model.dto.ExchangeRatesDto;

import java.util.List;

public class ExchangeRatesService {

    private static final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();

    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();

    private ExchangeRatesService() {}


    public List<ExchangeRatesDto> findAllExchangeRates() throws DataBaseIsNotAvalibleExeption {
        return exchangeRatesDao.findAllExchangeRates();
    }

    public ExchangeRatesDto findExchangeRate(String baseCode, String targetCode)
            throws DataBaseIsNotAvalibleExeption, ExchangeRateIsNotExistExeption {

        return exchangeRatesDao.findExchangeRate(baseCode, targetCode).orElseThrow(ExchangeRateIsNotExistExeption::new);
    }

    public ExchangeRatesDto createExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws DataBaseIsNotAvalibleExeption, ExchangeRateAlreadyExistExeption, ExchangeRateCodeDoesNotExistExeption {
        return exchangeRatesDao.createExchangeRate(new ExchangeRatesDto(null,
                new CurrencyDto(null, baseCurrencyCode, null, null),
                new CurrencyDto(null, targetCurrencyCode, null, null),
                rate)).orElseThrow(ExchangeRateAlreadyExistExeption::new);
    }


    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }
}
