package services;

import model.DataBaseIsNotAvalibleExeption;
import model.InvalidCodeExeption;
import model.dao.CurrencyDao;
import model.dto.CurrencyDto;

import java.util.Optional;

public class CurrencyService {

    private static final CurrencyService INSTANCE = new CurrencyService();

    private static final CurrencyDao currencyDao = CurrencyDao.getInstance();

    private CurrencyService(){}


    public Optional<CurrencyDto> findByCode(String code) throws InvalidCodeExeption, DataBaseIsNotAvalibleExeption {

    return currencyDao.findByCode(code)
            .map(dao -> new CurrencyDto(
                    dao.getId(),
                    dao.getCode(),
                    dao.getFullName(),
                    dao.getSign()
            ));

    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }


}
