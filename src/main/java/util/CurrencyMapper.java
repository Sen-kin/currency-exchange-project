package util;

import model.dto.CurrencyDTO;
import model.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyDTO toCurrencyDTO(Currency currency);

    Currency toCurrency(CurrencyDTO currencyDTO);

    List<CurrencyDTO> toCurrencyDTOList(List<Currency> currencies);
}
