package controllers;

import model.DataBaseIsNotAvalibleExeption;
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
        Double rate = null;

        if(rateStringValue.matches("^\\d+$")){
             rate = Double.parseDouble(rateStringValue);
        }

        if (baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty() || rateStringValue.isEmpty()){
            mapper.writeValue(resp.getWriter(), new ErrorDto("Отсутствует нужное поле формы"));
        }
    }
}


