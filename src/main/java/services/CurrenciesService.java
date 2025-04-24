package services;

import model.CurrencyAlreadyExistExeption;
import model.DataBaseIsNotAvalibleExeption;
import model.dto.CurrencyDto;
import model.dao.CurrenciesDao;
import model.entity.CurrencyEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrenciesService {

    private static final CurrenciesService INSTANCE = new CurrenciesService();

    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    private CurrenciesService(){}

    public List<CurrencyDto> findAll() throws ServiceExeption{
        try {
            return currenciesDao
                    .findAll()
                    .stream()
                    .map(dao -> new CurrencyDto(
                                    dao.getId(),
                                    dao.getCode(),
                                    dao.getFullName(),
                                    dao.getSign()
                            )
                    )
                    .collect(Collectors.toList());
        } catch (DataBaseIsNotAvalibleExeption e) {
            throw new ServiceExeption(e);
        }
    }

    public CurrencyDto createCurrency(CurrencyDto currencyDto) throws DataBaseIsNotAvalibleExeption, CurrencyAlreadyExistExeption {

        Optional<CurrencyEntity> createdEntity = currenciesDao.save(
                new CurrencyEntity(currencyDto.id(),
                        currencyDto.code(),
                        currencyDto.name(),
                        currencyDto.sign()
                ));

        if (createdEntity.isPresent()) {

            CurrencyEntity newCurrency = createdEntity.get();
            return new CurrencyDto(
                    newCurrency.getId(),
                    newCurrency.getCode(),
                    newCurrency.getFullName(),
                    newCurrency.getSign()
            );
        }
        return new CurrencyDto(null, "", "", "");
    }

    public static CurrenciesService getInstance() {
        return INSTANCE;
    }

}
