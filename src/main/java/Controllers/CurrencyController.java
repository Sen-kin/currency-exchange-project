package Controllers;

import Services.CurrenciesService;
import Services.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

@WebServlet("/currency/*")
public class CurrencyController extends HttpServlet {

    ObjectMapper mapper = new ObjectMapper();

    private final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        mapper.writeValue(resp.getWriter(), currencyService.findByCode(req.getPathInfo().substring(1)).get());
    }
}
