package controllers;

import model.DataBaseIsNotAvalibleExeption;
import model.ExchangeRateAlreadyExistExeption;
import model.ExchangeRateCodeDoesNotExistExeption;
import model.dto.ErrorDto;
import services.ExchangeRatesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesController extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        try {
            mapper.writeValue(resp.getWriter(), exchangeRatesService.findAllExchangeRates());
        } catch (DataBaseIsNotAvalibleExeption e) {
            mapper.writeValue(resp.getWriter(), new ErrorDto("Ошибка доступа к базе данных"));
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rateStringValue = req.getParameter("rate");


        if (baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty() || rateStringValue.isEmpty() || !rateStringValue.matches("^(\\d+(\\.\\d*)?|\\.\\d+)$")) {
            resp.setStatus(400);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Отсутствует нужное поле формы"));
        }

        Double rate = Double.parseDouble(rateStringValue);
        
        try {
            mapper.writeValue(resp.getWriter(), exchangeRatesService.createExchangeRate(baseCurrencyCode, targetCurrencyCode, rate));
        } catch (ExchangeRateAlreadyExistExeption e) {
            resp.setStatus(409);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Валютная пара с таким кодом уже существует"));
        } catch (DataBaseIsNotAvalibleExeption e) {
            resp.setStatus(500);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Ошибка доступа к базе данных"));
        } catch (ExchangeRateCodeDoesNotExistExeption e) {
            resp.setStatus(404);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Одна (или обе) валюта из валютной пары не существует в БД"));
        }
    }
}


