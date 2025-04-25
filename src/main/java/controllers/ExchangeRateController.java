package controllers;

import model.DataBaseIsNotAvalibleExeption;
import model.ExchangeRateIsNotExistExeption;
import model.dto.ErrorDto;
import services.ExchangeRatesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateController extends HttpServlet {

        private static final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();

        private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            resp.setContentType("application/json");

            String path = req.getPathInfo();

            if(path == null || path.length() != 7){
                resp.setStatus(400);
                mapper.writeValue(resp.getWriter(), new ErrorDto("Коды валют пары отсутствуют в адресе"));
            }

            String baseCode = path.substring(1, 4);
            String targetCode = path.substring(4,7);

            try {
                mapper.writeValue(resp.getWriter(), exchangeRatesService.findExchangeRate(baseCode, targetCode));

            } catch (DataBaseIsNotAvalibleExeption e) {
                resp.setStatus(500);
                mapper.writeValue(resp.getWriter(), new ErrorDto("Ошибка доступа к базе данных"));

            } catch (ExchangeRateIsNotExistExeption e) {
                resp.setStatus(404);
                mapper.writeValue(resp.getWriter(), new ErrorDto("Обменный курс для пары не найден"));
            }
    }
}
