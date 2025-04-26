package services;

import model.exceptions.CurrencyDoesNotExistException;
import model.exceptions.DataBaseIsNotAvalibleException;
import model.dao.CurrencyDao;
import model.dto.CurrencyDto;
import model.entity.CurrencyEntity;

public class CurrencyService {

    private static final CurrencyService INSTANCE = new CurrencyService();

    private static final CurrencyDao currencyDao = CurrencyDao.getInstance();

    private CurrencyService(){}


    public CurrencyDto findByCode(String code) throws DataBaseIsNotAvalibleException, CurrencyDoesNotExistException {

            CurrencyEntity currency = currencyDao.findByCode(code);

            return new CurrencyDto(
                            currency.getId(),
                            currency.getCode(),
                            currency.getName(),
                            currency.getSign()
            );
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }


}
