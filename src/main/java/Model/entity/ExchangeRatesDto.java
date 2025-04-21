package Model.entity;


import Model.dto.CurrencyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ExchangeRatesDto {

    private Long id;
    private CurrencyDto baseCurrency;
    private CurrencyDto targetCurrency;
    private Double rate;
}
