package model.dto;


public record CurrencyDto(
        Long id,
        String code,
        String name,
        String sign
) { }
