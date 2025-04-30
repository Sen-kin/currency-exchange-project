package models.dto;


public record ExchangeDTO(
        CurrencyDTO baseCurrency,
        CurrencyDTO targetCurrency,
        Double rate,
        Double amount,
        Double convertedAmount) {}
