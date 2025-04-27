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

    private static final JsonMapper mapper = JsonMapper.getInstance();

    private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        try {
            mapper.responseToJson(resp, exchangeRatesService.findAllExchangeRates());
        } catch (DataBaseIsNotAvailableException e) {
            mapper.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rateStringValue = req.getParameter("rate");


        if (StringUtils.isEmpty(baseCurrencyCode) || StringUtils.isEmpty(targetCurrencyCode) || StringUtils.isEmpty(rateStringValue) || !StringUtils.isNumeric(rateStringValue)) {
            resp.setStatus(400);
            mapper.responseToJson(resp, new ErrorDto("Отсутствует нужное поле формы"));
            return;
        }

        Double rate = Double.parseDouble(rateStringValue);

        try {
            mapper.responseToJson(resp, exchangeRatesService.createExchangeRate(baseCurrencyCode, targetCurrencyCode, rate));
        } catch (ExchangeRateAlreadyExistsException e) {
            resp.setStatus(409);
            mapper.responseToJson(resp, new ErrorDto("Валютная пара с таким кодом уже существует"));
        } catch (DataBaseIsNotAvailableException e) {
            resp.setStatus(500);
            mapper.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));
        } catch (ExchangeRateCodeDoesNotExistException e) {
            resp.setStatus(404);
            mapper.responseToJson(resp, new ErrorDto("Одна (или обе) валюта из валютной пары не существует в БД"));
        } catch (ExchangeRateCreationException e) {
            resp.setStatus(400);
            mapper.responseToJson(resp, new ErrorDto("Отсутствует нужное поле формы"));
        }
    }


}


