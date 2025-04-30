package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import repository.CurrencyRepository;
import repository.ExchangeRateRepository;
import services.CurrencyService;
import services.ExchangeRateService;
import mappers.JSONMapper;
import utils.PropertiesUtil;


@WebListener
public class ContextListener implements ServletContextListener {
    private static final String URL = "db.url";
    private static final String DRIVER = "db.driver";
    private static final String CROSS_CURRENCY = "USD";


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HikariDataSource dataSource = getDataSource();
        ServletContext context = sce.getServletContext();

        context.setAttribute("dataSource", dataSource);

        CurrencyRepository currencyRepository = new CurrencyRepository(dataSource);
        ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepository(dataSource);

        CurrencyService currencyService = new CurrencyService(currencyRepository);
        ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateRepository, currencyRepository, CROSS_CURRENCY);

        ObjectMapper mapper = new ObjectMapper();
        JSONMapper JSONMapper = new JSONMapper(mapper);

        context.setAttribute("currencyService", currencyService);
        context.setAttribute("exchangeRateService", exchangeRateService);
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
            config.setJdbcUrl(PropertiesUtil.get(URL));
            config.setDriverClassName(PropertiesUtil.get(DRIVER));
            config.setMaximumPoolSize(5);
            config.setConnectionTimeout(5000);
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Hikari Initialization failed");
        }
        return dataSource;
    }
}
