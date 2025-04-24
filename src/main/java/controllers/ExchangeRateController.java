package controllers;

import model.DataBaseIsNotAvailibleExeption;
import model.ExchangeRateIsNotExistExeption;
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
            String baseCode = "";
            String targetCode = "";

            if(path != null && path.length() == 7){
                baseCode = path.substring(1, 4);
                targetCode = path.substring(4,7);
            } else resp.sendError(400, "Отсутствует нужное поле формы");

            try {
                mapper.writeValue(resp.getWriter(), exchangeRatesService.findExchangeRateByCodes(baseCode, targetCode));
            }catch (DataBaseIsNotAvailibleExeption e) {
                if(!resp.isCommitted()) resp.sendError(500, "Ошибка доступа к базе данных");
            } catch (ExchangeRateIsNotExistExeption e) {
                if(!resp.isCommitted()) resp.sendError(404, "Обменный курс для пары не найден");
            }
    }
}
