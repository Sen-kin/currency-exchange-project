package controllers;

import model.DataBaseIsNotAvailibleExeption;
import model.InvalidCodeExeption;
import model.dto.CurrencyDto;
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
        String code = "";

        if  (path == null || path.length() != 4) resp.sendError(400, "Неправильный формат кода валюты(Код валюты должен состоять из 3-х символов");
                else code = path.substring(1).toUpperCase();
        try{
            CurrencyDto currency = currencyService.findByCode(code).orElseThrow(InvalidCodeExeption::new);
            mapper.writeValue(resp.getWriter(), currency);
        }catch (InvalidCodeExeption e){
            if (!resp.isCommitted()) resp.sendError(404, "Валюта не найдена");
        }catch (DataBaseIsNotAvailibleExeption e){
            if (!resp.isCommitted()) resp.sendError(500, "Ошибка доступа к базе данных");
        }
    }

}
