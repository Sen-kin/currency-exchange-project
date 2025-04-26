package services;

import model.dao.ExchangeRateDao;
import model.dto.CurrencyDto;
import model.dto.ExchangeRateDto;
import model.exceptions.*;

import java.util.List;

public class ExchangeRatesService {

    private static final ExchangeRateDao exchangeRatesDao = ExchangeRateDao.getInstance();

    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();

    private ExchangeRatesService() {}


    public List<ExchangeRateDto> findAllExchangeRates() throws DataBaseIsNotAvalibleException {
        return exchangeRatesDao.findAll();
    }

    public ExchangeRateDto findExchangeRate(String baseCode, String targetCode)
            throws DataBaseIsNotAvalibleException, ExchangeRateDoesNotExistException {

        return exchangeRatesDao.findByCodes(baseCode, targetCode);
    }

    public ExchangeRateDto createExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate)
            throws DataBaseIsNotAvalibleException, ExchangeRateAlreadyExistsException, ExchangeRateCodeDoesNotExistException, ExchangeRateCreationException {

        return exchangeRatesDao.create(
                new ExchangeRateDto(
                        null,
                        new CurrencyDto(null, baseCurrencyCode, null, null),
                        new CurrencyDto(null, targetCurrencyCode, null, null),
                        rate
                )
        );
    }

    public ExchangeRateDto updateExchangeRate(Double rate, String baseCurrencyCode, String targetCurrencyCode)
            throws DataBaseIsNotAvalibleException, ExchangeRateDoesNotExistException {
        return exchangeRatesDao.update(new ExchangeRateDto(
                null,
                new CurrencyDto(null, baseCurrencyCode, null, null),
                new CurrencyDto(null, targetCurrencyCode, null, null),
                rate
                )
        );
    }


    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }
}
