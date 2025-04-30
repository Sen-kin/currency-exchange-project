package controllers;

import jakarta.servlet.ServletContext;
import services.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.JSONMapper;
import utils.ValidationUtil;

import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesController extends HttpServlet {

    private ExchangeRateService exchangeRateService;
    private JSONMapper mapper;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        exchangeRateService = (ExchangeRateService) context.getAttribute("exchangeRateService");
        mapper = (JSONMapper) context.getAttribute("jsonMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mapper.writeResponseAsJson(resp, exchangeRateService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rateStringValue = req.getParameter("rate");

        ValidationUtil.exchangeRateCodesValidation(baseCurrencyCode, targetCurrencyCode);
        ValidationUtil.numberValidation(rateStringValue);

        Double rate = Double.parseDouble(rateStringValue);

        mapper.writeResponseAsJson(resp, exchangeRateService.create(baseCurrencyCode, targetCurrencyCode, rate));
    }
}



