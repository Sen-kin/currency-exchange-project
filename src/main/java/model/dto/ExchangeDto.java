package model.dto;


public record ExchangeDto(
        CurrencyDto baseCurrency,
        CurrencyDto targetCurrency,
        Double rate,
        Double amount,
        Double convertedAmount) {}
