package controllers;

import model.exceptions.DataBaseIsNotAvalibleException;
import model.exceptions.ExchangeRateDoesNotExistException;
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

            } catch (DataBaseIsNotAvalibleException e) {
                resp.setStatus(500);
                mapper.writeValue(resp.getWriter(), new ErrorDto("Ошибка доступа к базе данных"));

            } catch (ExchangeRateDoesNotExistException e) {
                resp.setStatus(404);
                mapper.writeValue(resp.getWriter(), new ErrorDto("Обменный курс для пары не найден"));
            }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String path = req.getPathInfo();

        String baseCurrencyCode = path.substring(1, 4);
        String targetCurrencyCode = path.substring(4, 7);
        String rateStringValue = req.getParameter("rate");

        System.out.println(rateStringValue);

        if (!rateStringValue.matches("^(\\d+(\\.\\d*)?|\\.\\d+)$")){
            resp.setStatus(400);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Отсутствует нужное поле формы"));
        }

        Double rate = Double.parseDouble(rateStringValue);

        try{
            mapper.writeValue(resp.getWriter(), exchangeRatesService.updateExchangeRate(rate, baseCurrencyCode, targetCurrencyCode));
        }catch (DataBaseIsNotAvalibleException e) {
            resp.setStatus(500);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Ошибка доступа к базе данных"));
        }catch (ExchangeRateDoesNotExistException e) {
            resp.setStatus(404);
            mapper.writeValue(resp.getWriter(), new ErrorDto("Валютная пара отсутствует в базе данных"));
        }

    }

}
