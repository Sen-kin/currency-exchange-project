package services;

import model.DataBaseIsNotAvalibleExeption;
import model.ExchangeRateIsNotExistExeption;
import model.dao.ExchangeRatesDao;
import model.dto.ExchangeRatesDto;

import java.util.List;

public class ExchangeRatesService {

    private static final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();

    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();

    private ExchangeRatesService() {}


    public List<ExchangeRatesDto> findAllExchangeRates() throws DataBaseIsNotAvalibleExeption {
        return exchangeRatesDao.findAllExchangeRates();
    }

    public ExchangeRatesDto findExchangeRateByCodes(String baseCode, String targetCode)
            throws DataBaseIsNotAvalibleExeption, ExchangeRateIsNotExistExeption {

        return exchangeRatesDao.findExchangeRate(baseCode, targetCode).orElseThrow(ExchangeRateIsNotExistExeption::new);
    }


    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }
}
