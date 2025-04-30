package controllers;

import java.io.*;

import jakarta.servlet.ServletContext;
import models.dto.CurrencyDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.CurrencyService;
import mappers.JSONMapper;
import utils.ValidationUtil;

@WebServlet(value = "/currencies")
public class CurrenciesController extends HttpServlet {

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
            mapper.writeResponseAsJson(resp, currencyService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");
        ValidationUtil.currencyValidation(code, name, sign);

        CurrencyDTO currencyDTO = new CurrencyDTO(null, code, name, sign);

        CurrencyDTO createdCurrency = currencyService.create(currencyDTO);
        mapper.writeResponseAsJson(resp, createdCurrency);
    }
}


