package services;

import models.dto.CurrencyDTO;
import models.entity.Currency;
import models.entity.ExchangeRate;
import repository.CurrencyRepository;
import repository.ExchangeRateRepository;
import models.dto.ExchangeDTO;
import models.dto.ExchangeRateDTO;
import exceptions.*;
import mappers.CurrencyMapper;
import mappers.ExchangeRateMapper;

import java.util.List;

public class ExchangeRateService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyMapper currencyMapper;
    private final ExchangeRateMapper exchangeRateMapper;
    private final String crossRateCurrency;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, CurrencyRepository currencyRepository, String crossRateCurrency) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyMapper = CurrencyMapper.INSTANCE;
        this.exchangeRateMapper = ExchangeRateMapper.INSTANCE;
        this.crossRateCurrency = crossRateCurrency;
    }

    public List<ExchangeRateDTO> findAll() {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();

        return exchangeRateMapper.toExchangeRateDTOList(exchangeRates);
    }

    public ExchangeRateDTO find(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRate foundedExchangeRate = exchangeRateRepository.find(baseCurrencyCode, targetCurrencyCode);

        if (foundedExchangeRate == null) throw new ExchangeRateDoesNotExistException("Exchange rate not found");

        return exchangeRateMapper.toExchangeRateDTO(foundedExchangeRate);
    }

    public ExchangeRateDTO create(String baseCurrencyCode, String targetCurrencyCode, Double rate) {
        Currency baseCurrency = currencyRepository.find(baseCurrencyCode);
        Currency targetCurrency = currencyRepository.find(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null)
            throw new CurrencyDoesNotExistException("Currency not found");

        ExchangeRate createdExchangeRate = exchangeRateRepository.create(new ExchangeRate(null, baseCurrency, targetCurrency, rate));

        return exchangeRateMapper.toExchangeRateDTO(createdExchangeRate);
    }

    public ExchangeRateDTO update(String baseCurrencyCode, String targetCurrencyCode, Double rate) {
        ExchangeRate updatedExchangeRate = exchangeRateRepository.update(baseCurrencyCode, targetCurrencyCode, rate);

        return exchangeRateMapper.toExchangeRateDTO(updatedExchangeRate);
    }

    public ExchangeDTO exchange(String baseCurrencyCode, String targetCurrencyCode, Double amount) {
        Double rate;
        ExchangeRate exchangeRate = exchangeRateRepository.find(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate == null) {
            ExchangeRate reverseRate = exchangeRateRepository.find(targetCurrencyCode, baseCurrencyCode);
            if (reverseRate == null) {
                ExchangeRate baseToIntermediate = exchangeRateRepository.find(baseCurrencyCode, crossRateCurrency);
                ExchangeRate intermediateToTarget = exchangeRateRepository.find(crossRateCurrency, targetCurrencyCode);
                if (baseToIntermediate == null || intermediateToTarget == null) {
                    throw new ExchangeRateDoesNotExistException("Failed to exchange");
                }
                rate = intermediateToTarget.getRate() * baseToIntermediate.getRate();
            } else rate = 1.0 / reverseRate.getRate();
        } else rate = exchangeRate.getRate();

        Double convertedAmount = rate * amount;

        Currency baseCurrency = currencyRepository.find(baseCurrencyCode);
        Currency targetCurrency = currencyRepository.find(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new CurrencyDoesNotExistException("Currency not found");
        }

        CurrencyDTO baseCurrencyDTO = currencyMapper.toCurrencyDTO(baseCurrency);
        CurrencyDTO targetCurrencyDTO = currencyMapper.toCurrencyDTO(targetCurrency);

        return new ExchangeDTO(baseCurrencyDTO, targetCurrencyDTO, rate, amount, convertedAmount);
    }
}

