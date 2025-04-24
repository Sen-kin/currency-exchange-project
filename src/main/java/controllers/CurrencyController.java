package controllers;

import model.DataBaseIsNotAvalibleExeption;
import model.InvalidCodeExeption;
import model.dto.CurrencyDto;
import model.dto.ErrorDto;
import services.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyController extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String path = req.getPathInfo();

        if  (path == null || path.length() != 4){
            mapper.writeValue(resp.getWriter(), new ErrorDto("Код валюты отсутствует в адресе"));
        }

        String code = path.substring(1).toUpperCase();

        try{
            CurrencyDto currency = currencyService.findByCode(code).orElseThrow(InvalidCodeExeption::new);
            mapper.writeValue(resp.getWriter(), currency);
        }catch (InvalidCodeExeption e){
            if (!resp.isCommitted()) mapper.writeValue(resp.getWriter(), new ErrorDto("Валюта не найдена"));
        }catch (DataBaseIsNotAvalibleExeption e){
            if (!resp.isCommitted()) mapper.writeValue(resp.getWriter(), new ErrorDto("Ошибка доступа к базе данных"));
        }

    }

}
