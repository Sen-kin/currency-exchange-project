package controllers;

import model.exceptions.DataBaseIsNotAvailableException;
import model.exceptions.ExchangeRateAlreadyExistsException;
import model.exceptions.ExchangeRateCodeDoesNotExistException;
import model.exceptions.ExchangeRateCreationException;
import model.dto.ErrorDto;
import org.apache.commons.lang3.StringUtils;
import services.ExchangeRatesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesController extends HttpServlet {

    private final ExchangeRatesService EXCHANGE_RATES_SERVICE = ExchangeRatesService.getInstance();

    private static final JsonMapper MAPPER = JsonMapper.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        try {
            MAPPER.responseToJson(resp, EXCHANGE_RATES_SERVICE.findAllExchangeRates());
        } catch (DataBaseIsNotAvailableException e) {
            MAPPER.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rateStringValue = req.getParameter("rate");


        if (StringUtils.isEmpty(baseCurrencyCode) || StringUtils.isEmpty(targetCurrencyCode) || StringUtils.isEmpty(rateStringValue) || !StringUtils.isNumeric(rateStringValue)) {
            resp.setStatus(400);
            MAPPER.responseToJson(resp, new ErrorDto("Отсутствует нужное поле формы"));
            return;
        }

        Double rate = Double.parseDouble(rateStringValue);

        try {
            MAPPER.responseToJson(resp, EXCHANGE_RATES_SERVICE.createExchangeRate(baseCurrencyCode, targetCurrencyCode, rate));
        } catch (ExchangeRateAlreadyExistsException e) {
            resp.setStatus(409);
            MAPPER.responseToJson(resp, new ErrorDto("Валютная пара с таким кодом уже существует"));
        } catch (DataBaseIsNotAvailableException e) {
            resp.setStatus(500);
            MAPPER.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));
        } catch (ExchangeRateCodeDoesNotExistException e) {
            resp.setStatus(404);
            MAPPER.responseToJson(resp, new ErrorDto("Одна (или обе) валюта из валютной пары не существует в БД"));
        } catch (ExchangeRateCreationException e) {
            resp.setStatus(400);
            MAPPER.responseToJson(resp, new ErrorDto("Отсутствует нужное поле формы"));
        }
    }
}


