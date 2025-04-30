package controllers;

import jakarta.servlet.ServletContext;
import models.dto.CurrencyDTO;
import services.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.JSONMapper;
import utils.ValidationUtil;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyController extends HttpServlet {

    private CurrencyService currencyService;
    private JSONMapper mapper;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        currencyService = (CurrencyService) context.getAttribute("currencyService");
        mapper = (JSONMapper) context.getAttribute("jsonMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        ValidationUtil.pathCurrencyValidation(path);
        String code = path.substring(1);

        CurrencyDTO currency = currencyService.find(code);
        mapper.writeResponseAsJson(resp, currency);
    }
}
