package controllers;

import java.io.*;

import model.exceptions.CurrencyAlreadyExistsException;
import model.exceptions.CurrencyCreationException;
import model.exceptions.DataBaseIsNotAvalibleException;
import model.dto.CurrencyDto;
import model.dto.ErrorDto;
import services.CurrenciesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(value = "/currencies")
public class CurrenciesController extends HttpServlet {

    private final CurrenciesService currenciesService = CurrenciesService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        try {

            mapper.writeValue(resp.getWriter(), currenciesService.findAll());

        } catch (DataBaseIsNotAvalibleException e) {
            resp.setStatus(500);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Ошибка доступа к базе данных"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        if (code == null || name == null || sign == null || code.isEmpty() || name.isEmpty() || sign.isEmpty())
        {
            mapper.writeValue(resp.getWriter(), new ErrorDto("Отсутствует нужное поле формы"));
        }

        try {

            CurrencyDto newCurrency = currenciesService.createCurrency(new CurrencyDto(null, code, name, sign));

            mapper.writeValue(resp.getWriter(), newCurrency);

        } catch (CurrencyCreationException e) {
            resp.setStatus(400);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Отсутствует нужное поле формы"));
        } catch (CurrencyAlreadyExistsException e){
            resp.setStatus(409);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Валюта с таким кодом уже существует"));
        } catch (DataBaseIsNotAvalibleException e) {
            resp.setStatus(500);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Ошибка доступа к базе данных"));
        }
    }
 }


