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

    private final CurrenciesService CURRENCY_SERVICE = CurrenciesService.getInstance();

    private final JsonMapper MAPPER = JsonMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        try {

            MAPPER.responseToJson(resp, CURRENCY_SERVICE.findAll());

        } catch (DataBaseIsNotAvailableException e) {
            resp.setStatus(500);
            MAPPER.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));
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
            MAPPER.responseToJson(resp, new ErrorDto("Отсутствует нужное поле формы"));
            return;
        }

        try {

            CurrencyDto newCurrency = CURRENCY_SERVICE.createCurrency(new CurrencyDto(null, code, name, sign));

            MAPPER.responseToJson(resp, newCurrency);

        } catch (CurrencyCreationException e) {
            resp.setStatus(400);
            MAPPER.responseToJson(resp, new ErrorDto("Отсутствует нужное поле формы"));
        } catch (CurrencyAlreadyExistsException e){
            resp.setStatus(409);
            MAPPER.responseToJson(resp, new ErrorDto("Валюта с таким кодом уже существует"));
        } catch (DataBaseIsNotAvailableException e) {
            resp.setStatus(500);
            MAPPER.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));
        }
    }
 }


