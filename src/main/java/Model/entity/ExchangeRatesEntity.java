package Model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ExchangeRatesEntity {

    private Long Id;
    private Long BaseCurrencyId;
    private Long TargetCurrencyId;
    private Double Rate;
}
