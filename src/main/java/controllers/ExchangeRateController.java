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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


@WebServlet("/exchangeRate/*")
public class ExchangeRateController extends HttpServlet {

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
        String path = req.getPathInfo();
        ValidationUtil.pathExchangeRateValidation(path);
        String baseCurrencyCode = path.substring(1, 4);
        String targetCurrencyCode = path.substring(4, 7);

        mapper.writeResponseAsJson(resp, exchangeRateService.find(baseCurrencyCode, targetCurrencyCode));
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        ValidationUtil.pathExchangeRateValidation(path);

        String baseCurrencyCode = path.substring(1, 4);
        String targetCurrencyCode = path.substring(4, 7);
        String rateStringValue = rateReturningFromPatchHelper(req);

        ValidationUtil.exchangeCodesValidation(baseCurrencyCode, targetCurrencyCode);
        ValidationUtil.numberValidation(rateStringValue);

        Double rate = Double.parseDouble(rateStringValue);

        mapper.writeResponseAsJson(resp, exchangeRateService.update(baseCurrencyCode, targetCurrencyCode, rate));
    }

    private static String rateReturningFromPatchHelper(HttpServletRequest req) throws IOException {
        InputStream input = req.getInputStream();
        Scanner scanner = new Scanner(input, StandardCharsets.UTF_8);
        String rateStringValue = scanner.nextLine();

        return rateStringValue.replaceAll("rate=", "").replaceAll("%2", ".");
    }
}
