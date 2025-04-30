package models.dto;


public record CurrencyDTO(
        Long id,
        String code,
        String name,
        String sign
) {}
