package filters;

import exceptions.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import models.dto.ErrorDTO;
import mappers.JSONMapper;

import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class ExceptionFilter implements Filter {
    private JSONMapper mapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        mapper = (JSONMapper) filterConfig.getServletContext().getAttribute("jsonMapper");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            switch (e) {
                case IllegalArgumentException illArgExc -> {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    illArgExc.getStackTrace();
                }
                case ExchangeRateDoesNotExistException doesntExExc -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    doesntExExc.getStackTrace();
                }
                case ExchangeRateAlreadyExistsException alrExExc -> {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    alrExExc.getStackTrace();
                }
                case CurrencyDoesNotExistException doesntExExc -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    doesntExExc.getStackTrace();
                }
                case CurrencyAlreadyExistsException alrExExc -> {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    alrExExc.getStackTrace();
                }
                case DataBaseAccessException dbAccExc -> {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    dbAccExc.getStackTrace();
                }
                default -> log.error("Unexpected error", e);
            }

            mapper.writeResponseAsJson(servletResponse, new ErrorDTO(e.getMessage()));
        }
    }
}
