package Services;

import Model.dto.CurrencyDto;
import Model.dao.CurrenciesDao;

import java.util.List;
import java.util.stream.Collectors;

public class CurrenciesService {

    private static final CurrenciesService INSTANCE = new CurrenciesService();

    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    private CurrenciesService(){}

    public List<CurrencyDto> findAll(){
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
    }

    public static CurrenciesService getInstance() {
        return INSTANCE;
    }


}
