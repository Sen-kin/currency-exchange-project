package mappers;

import models.dto.ExchangeRateDTO;
import models.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ExchangeRateMapper {

    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateDTO toExchangeRateDTO (ExchangeRate exchangeRate);

    ExchangeRate toExchangeRate (ExchangeRateDTO exchangeRateDTO);

    List<ExchangeRateDTO> toExchangeRateDTOList (List<ExchangeRate> exchangeRates);
}
