package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import repository.CurrencyRepository;
import repository.ExchangeRateRepository;
import service.CurrencyService;
import service.ExchangeRateService;
import util.JSONMapper;


@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HikariDataSource dataSource = getDataSource();
        ServletContext context = sce.getServletContext();

        context.setAttribute("dataSource", dataSource);

        CurrencyRepository currencyDao = new CurrencyRepository(dataSource);
        ExchangeRateRepository exchangeRateDao = new ExchangeRateRepository(dataSource);

        CurrencyService currencyService = new CurrencyService(currencyDao);
        ExchangeRateService exchangeRatesService = new ExchangeRateService(exchangeRateDao, currencyDao, "USD");

        ObjectMapper mapper = new ObjectMapper();
        JSONMapper JSONMapper = new JSONMapper(mapper);

        context.setAttribute("currencyService", currencyService);
        context.setAttribute("exchangeRatesService", exchangeRatesService);
        context.setAttribute("jsonMapper", JSONMapper);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HikariDataSource dataSource = (HikariDataSource) sce.getServletContext().getAttribute("dataSource");
        if (dataSource != null) {
            dataSource.close();
        }
    }

    private static HikariDataSource getDataSource() {
        HikariDataSource dataSource;
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:sqlite::resource:identifier.sqlite");
            config.setDriverClassName("org.sqlite.JDBC");
            config.setMaximumPoolSize(5);
            config.setConnectionTimeout(5000);
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Hikari Initialization failed");
        }
        return dataSource;
    }
}
