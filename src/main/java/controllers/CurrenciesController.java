package controllers;

import java.io.*;

import model.CurrencyAlreadyExistExeption;
import model.DataBaseIsNotAvailibleExeption;
import model.dto.CurrencyDto;
import services.CurrenciesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.ServiceExeption;

@WebServlet(value = "/currencies")
public class CurrenciesController extends HttpServlet {

    private final CurrenciesService currenciesService = CurrenciesService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("application/json");

            mapper.writeValue(resp.getWriter(), currenciesService.findAll());
        } catch (ServiceExeption e) {
            resp.sendError(500, "Ошибка доступа к базе данных");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

            String name = req.getParameter("name");
            String code = req.getParameter("code").toUpperCase();
            String sign = req.getParameter("sign");

        System.out.println(code + name + sign);


            if (code.isEmpty() || name.isEmpty() || sign.isEmpty()) resp.sendError(400, "Отсутствует нужное поле формы");

            try {
               CurrencyDto newCurrency = currenciesService.createCurrency(new CurrencyDto(null, code, name, sign));
                mapper.writeValue(resp.getWriter(), newCurrency);
                resp.setStatus(201);
            } catch (CurrencyAlreadyExistExeption e){
                if(!resp.isCommitted()) resp.sendError(409, "Валюта с таким кодом уже существует");
            } catch (DataBaseIsNotAvailibleExeption e) {
                if(!resp.isCommitted()) resp.sendError(500, "Ошибка доступа к базе данных");
            }

    }
 }


