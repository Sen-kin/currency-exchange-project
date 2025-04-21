package Controllers;

import Services.ExchangeRatesService;
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

            String baseCode = req.getPathInfo().substring(1, 4);
            String targetCode = req.getPathInfo().substring(4);

        System.out.println(baseCode + " " + targetCode);;

                mapper.writeValue(resp.getWriter(), exchangeRatesService.findExchangeRateByCodes(baseCode, targetCode));
    }
}
