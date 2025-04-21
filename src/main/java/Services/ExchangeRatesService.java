package Services;

import Model.dao.ExchangeRatesDao;
import Model.entity.ExchangeRatesDto;

import java.util.List;

public class ExchangeRatesService {

    private static final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();

    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();

    private ExchangeRatesService() {}


    public List<ExchangeRatesDto> findAllExchangeRates(){
        return exchangeRatesDao.findAllExchangeRates();
    }

    public ExchangeRatesDto findExchangeRateByCodes(String baseCode, String targetCode){
        return exchangeRatesDao.findExchangeRate(baseCode, targetCode).get();
    }


    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }
}
