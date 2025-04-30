package controller;

import jakarta.servlet.ServletContext;
import service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JSONMapper;
import util.ValidationUtil;

import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesController extends HttpServlet {

    private ExchangeRateService exchangeRatesService;
    private JSONMapper mapper;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        exchangeRatesService = (ExchangeRateService) context.getAttribute("exchangeRatesService");
        mapper = (JSONMapper) context.getAttribute("jsonMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mapper.writeResponseAsJson(resp, exchangeRatesService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rateStringValue = req.getParameter("rate");

        ValidationUtil.exchangeCodesValidation(baseCurrencyCode, targetCurrencyCode);
        ValidationUtil.numberValidation(rateStringValue);

        Double rate = Double.parseDouble(rateStringValue);

        mapper.writeResponseAsJson(resp, exchangeRatesService.create(baseCurrencyCode, targetCurrencyCode, rate));
    }
}



