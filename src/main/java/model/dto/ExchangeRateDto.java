package model.dto;


public record ExchangeRateDto(
        Long id,
        CurrencyDto baseCurrency,
        CurrencyDto targetCurrency,
        Double rate) { }
