package models.dto;


public record ExchangeRateDTO(
        Long id,
        CurrencyDTO baseCurrency,
        CurrencyDTO targetCurrency,
        Double rate
) {}
