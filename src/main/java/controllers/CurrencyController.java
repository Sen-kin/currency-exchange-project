package controllers;

import model.exceptions.CurrencyDoesNotExistException;
import model.exceptions.DataBaseIsNotAvailableException;
import model.dto.CurrencyDto;
import model.dto.ErrorDto;
import services.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyController extends HttpServlet {

    private final CurrencyService CURRENCY_SERVICE = CurrencyService.getInstance();

    private final JsonMapper MAPPER = JsonMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String path = req.getPathInfo();

        if  (path == null || path.length() != 4)
        {
            MAPPER.responseToJson(resp, new ErrorDto("Код валюты отсутствует в адресе"));
            return;
        }

        String code = path.substring(1).toUpperCase();

        try{
            CurrencyDto currency = CURRENCY_SERVICE.findByCode(code);

            MAPPER.responseToJson(resp, currency);

        } catch (CurrencyDoesNotExistException e){

            resp.setStatus(404);
            MAPPER.responseToJson(resp, new ErrorDto("Валюта не найдена"));

        } catch (DataBaseIsNotAvailableException e){

            resp.setStatus(500);
            MAPPER.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));

        }

    }

}
