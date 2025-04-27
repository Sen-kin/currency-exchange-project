package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.ErrorDto;
import model.exceptions.DataBaseIsNotAvailableException;
import model.exceptions.ExchangeRateDoesNotExistException;
import org.apache.commons.lang3.StringUtils;
import services.ExchangeRatesService;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeController extends HttpServlet {

    ExchangeRatesService EXCHANGE_RATES_SERVICE = ExchangeRatesService.getInstance();

    JsonMapper MAPPER = JsonMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amountStringValue = req.getParameter("amount");

        if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to) || !StringUtils.isNumeric(amountStringValue)) {
            resp.setStatus(400);
            MAPPER.responseToJson(resp, new ErrorDto("Отсутствует нужное поле формы"));
            return;
        }

        Double amount = Double.parseDouble(amountStringValue);

        try {
            MAPPER.responseToJson(resp, EXCHANGE_RATES_SERVICE.exchange(from, to, amount));
        } catch (DataBaseIsNotAvailableException e) {
            resp.setStatus(500);
            MAPPER.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));
        } catch (ExchangeRateDoesNotExistException e) {
            resp.setStatus(404);
            MAPPER.responseToJson(resp, new ErrorDto("Ошибка выполнения обмена"));
        }
    }
}

