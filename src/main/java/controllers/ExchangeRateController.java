package controllers;

import model.exceptions.DataBaseIsNotAvailableException;
import model.exceptions.ExchangeRateDoesNotExistException;
import model.dto.ErrorDto;
import org.apache.commons.lang3.StringUtils;
import services.ExchangeRatesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateController extends HttpServlet {

        private static final ExchangeRatesService EXCHANGE_RATES_SERVICE = ExchangeRatesService.getInstance();

        private static final JsonMapper MAPPER = JsonMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            resp.setContentType("application/json");

            String path = req.getPathInfo();

            if(path == null || path.length() != 7){
                resp.setStatus(400);
                MAPPER.responseToJson(resp, new ErrorDto("Коды валют пары отсутствуют в адресе"));
                return;
            }

            String baseCode = path.substring(1, 4);
            String targetCode = path.substring(4,7);

            try {
                MAPPER.responseToJson(resp, EXCHANGE_RATES_SERVICE.findExchangeRate(baseCode, targetCode));

            } catch (DataBaseIsNotAvailableException e) {
                resp.setStatus(500);
                MAPPER.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));

            } catch (ExchangeRateDoesNotExistException e) {
                resp.setStatus(404);
                MAPPER.responseToJson(resp, new ErrorDto("Обменный курс для пары не найден"));
            }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String path = req.getPathInfo();

        if(path == null || path.length() != 7){
            resp.setStatus(400);
            MAPPER.responseToJson(resp, new ErrorDto("Коды валют пары отсутствуют в адресе"));
            return;
        }

        String baseCurrencyCode = path.substring(1, 4);
        String targetCurrencyCode = path.substring(4, 7);
        String rateStringValue = req.getParameter("rate");

        if (!StringUtils.isNumeric(rateStringValue)) {
            resp.setStatus(400);
            MAPPER.responseToJson(resp, new ErrorDto("Отсутствует нужное поле формы"));
            return;
        }

        Double rate = Double.parseDouble(rateStringValue);

        try{
            MAPPER.responseToJson(resp, EXCHANGE_RATES_SERVICE.updateExchangeRate(baseCurrencyCode, targetCurrencyCode, rate));
        }catch (DataBaseIsNotAvailableException e) {
            resp.setStatus(500);
            MAPPER.responseToJson(resp, new ErrorDto("Ошибка доступа к базе данных"));
        }catch (ExchangeRateDoesNotExistException e) {
            resp.setStatus(404);
            MAPPER.responseToJson(resp, new ErrorDto("Валютная пара отсутствует в базе данных"));
        }
    }
}
