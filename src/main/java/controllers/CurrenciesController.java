package controllers;

import java.io.*;

import model.exceptions.CurrencyAlreadyExistsException;
import model.exceptions.CurrencyCreationException;
import model.exceptions.DataBaseIsNotAvailableException;
import model.dto.CurrencyDto;
import model.dto.ErrorDto;
import org.apache.commons.lang3.StringUtils;
import services.CurrenciesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(value = "/currencies")
public class CurrenciesController extends HttpServlet {

    private final CurrenciesService currenciesService = CurrenciesService.getInstance();

    private final JsonMapper mapper = JsonMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        try {

            mapper.responseToJson(resp, currenciesService.findAll());

        } catch (DataBaseIsNotAvailableException e) {
            resp.setStatus(500);
            mapper.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(name) || StringUtils.isEmpty(sign)) {
            resp.setStatus(400);
            mapper.responseToJson(resp, new ErrorDto("Отсутствует нужное поле формы"));
            return;
        }

        try {

            CurrencyDto newCurrency = currenciesService.createCurrency(new CurrencyDto(null, code, name, sign));

            mapper.responseToJson(resp, newCurrency);

        } catch (CurrencyCreationException e) {
            resp.setStatus(400);
            mapper.responseToJson(resp, new ErrorDto("Отсутствует нужное поле формы"));
        } catch (CurrencyAlreadyExistsException e){
            resp.setStatus(409);
            mapper.responseToJson(resp, new ErrorDto("Валюта с таким кодом уже существует"));
        } catch (DataBaseIsNotAvailableException e) {
            resp.setStatus(500);
            mapper.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));
        }
    }
 }


