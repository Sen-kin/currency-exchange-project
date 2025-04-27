package services;

import model.dao.ExchangeRateDao;
import model.dto.CurrencyDto;
import model.dto.ExchangeDto;
import model.dto.ExchangeRateDto;
import model.exceptions.*;

import java.util.List;

public class ExchangeRatesService {

    private static final ExchangeRateDao exchangeRatesDao = ExchangeRateDao.getInstance();

    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();

    private static final String crossRateCurrency = "USD";

    private ExchangeRatesService() {}

    public ExchangeRateDto createExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate)
            throws DataBaseIsNotAvailableException, ExchangeRateAlreadyExistsException, ExchangeRateCodeDoesNotExistException, ExchangeRateCreationException {

        return exchangeRatesDao.create(
                new ExchangeRateDto(
                        null,
                        new CurrencyDto(null, baseCurrencyCode, null, null),
                        new CurrencyDto(null, targetCurrencyCode, null, null),
                        rate
                )
        );
    }

    public List<ExchangeRateDto> findAllExchangeRates() throws DataBaseIsNotAvailableException {
        return exchangeRatesDao.findAll();
    }

    public ExchangeRateDto findExchangeRate(String baseCode, String targetCode)
            throws DataBaseIsNotAvailableException, ExchangeRateDoesNotExistException {

        return exchangeRatesDao.findByCodes(baseCode, targetCode);
    }

    public ExchangeRateDto updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate)
            throws DataBaseIsNotAvailableException, ExchangeRateDoesNotExistException {
        return exchangeRatesDao.update(new ExchangeRateDto(
                null,
                new CurrencyDto(null, baseCurrencyCode, null, null),
                new CurrencyDto(null, targetCurrencyCode, null, null),
                rate
                )
        );
    }

    public ExchangeDto exchange(String from, String to, Double amount) throws DataBaseIsNotAvailableException, ExchangeRateDoesNotExistException{

       try {

           ExchangeRateDto AB = findExchangeRate(from, to);
           return new ExchangeDto(
                   new CurrencyDto(
                           AB.baseCurrency().id(),
                           AB.baseCurrency().name(),
                           AB.baseCurrency().code(),
                           AB.baseCurrency().sign()
                           ),
                   new CurrencyDto(
                           AB.targetCurrency().id(),
                           AB.targetCurrency().name(),
                           AB.targetCurrency().code(),
                           AB.targetCurrency().sign()
                   ),
                   AB.rate(), amount, AB.rate() * amount
           );

       } catch (ExchangeRateDoesNotExistException e) {
           try {

               ExchangeRateDto BA = findExchangeRate(to, from);

               return new ExchangeDto(
                       new CurrencyDto(
                               BA.baseCurrency().id(),
                               BA.baseCurrency().name(),
                               BA.baseCurrency().code(),
                               BA.baseCurrency().sign()
                       ),
                       new CurrencyDto(
                               BA.targetCurrency().id(),
                               BA.targetCurrency().name(),
                               BA.targetCurrency().code(),
                               BA.targetCurrency().sign()
                       ),
                       BA.rate(), amount, BA.rate() * amount
               );

           } catch (ExchangeRateDoesNotExistException e2){

               ExchangeRateDto A_XXX = findExchangeRate(from, crossRateCurrency);
               ExchangeRateDto XXX_B = findExchangeRate(crossRateCurrency, to);

                   return new ExchangeDto(
                           new CurrencyDto(
                                   A_XXX.baseCurrency().id(),
                                   A_XXX.baseCurrency().name(),
                                   A_XXX.baseCurrency().code(),
                                   A_XXX.baseCurrency().sign()
                           ),
                           new CurrencyDto(
                                   XXX_B.targetCurrency().id(),
                                   XXX_B.targetCurrency().name(),
                                   XXX_B.targetCurrency().code(),
                                   XXX_B.targetCurrency().sign()
                           ),
                           XXX_B.rate() / A_XXX.rate(), amount,(XXX_B.rate() / A_XXX.rate()) * amount


                   );

           }
       }
    }

    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }
}
