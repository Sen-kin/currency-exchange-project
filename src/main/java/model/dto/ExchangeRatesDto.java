package model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public record ExchangeRatesDto(
        Long id,
        CurrencyDto baseCurrency,
        CurrencyDto targetCurrency,
        Double rate) { }
