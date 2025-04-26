package services;

import model.exceptions.CurrencyAlreadyExistsException;
import model.exceptions.CurrencyCreationException;
import model.exceptions.DataBaseIsNotAvalibleException;
import model.dto.CurrencyDto;
import model.dao.CurrenciesDao;
import model.entity.CurrencyEntity;

import java.util.List;
import java.util.stream.Collectors;

public class CurrenciesService {

    private static final CurrenciesService INSTANCE = new CurrenciesService();

    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    private CurrenciesService(){}

    public List<CurrencyDto> findAll() throws DataBaseIsNotAvalibleException {
            return currenciesDao
                    .findAll()
                    .stream()
                    .map(dao -> new CurrencyDto(
                                    dao.getId(),
                                    dao.getCode(),
                                    dao.getName(),
                                    dao.getSign()
                            )
                    )
                    .collect(Collectors.toList());

    }

    public CurrencyDto createCurrency(CurrencyDto currencyDto) throws DataBaseIsNotAvalibleException, CurrencyAlreadyExistsException, CurrencyCreationException {

        CurrencyEntity createdCurrency = currenciesDao.create(
                new CurrencyEntity(
                        currencyDto.id(),
                        currencyDto.code(),
                        currencyDto.name(),
                        currencyDto.sign()
                ));

            return new CurrencyDto(
                    createdCurrency.getId(),
                    createdCurrency.getCode(),
                    createdCurrency.getName(),
                    createdCurrency.getSign()
            );
    }

    public static CurrenciesService getInstance() {
        return INSTANCE;
    }

}
