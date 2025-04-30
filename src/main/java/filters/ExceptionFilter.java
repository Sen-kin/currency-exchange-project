package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import model.dto.ErrorDTO;
import util.JSONMapper;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionFilter implements Filter {
    private JSONMapper mapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        mapper = (JSONMapper) filterConfig.getServletContext().getAttribute("jsonMapper");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            mapper.writeResponseAsJson(servletResponse, new ErrorDTO(e.getMessage()));
        }
    }
}
