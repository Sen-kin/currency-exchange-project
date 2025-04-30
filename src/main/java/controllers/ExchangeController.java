package controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ExchangeRateService;
import mappers.JSONMapper;
import utils.ValidationUtil;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeController extends HttpServlet {

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
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amountStringValue = req.getParameter("amount");

        ValidationUtil.exchangeRateCodesValidation(baseCurrencyCode, targetCurrencyCode);
        ValidationUtil.numberValidation(amountStringValue);

        Double amount = Double.parseDouble(amountStringValue);

        mapper.writeResponseAsJson(resp, exchangeRateService.exchange(baseCurrencyCode, targetCurrencyCode, amount));
    }
}

