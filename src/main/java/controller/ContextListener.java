package controller;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import repository.CurrenciesDao;
import repository.CurrencyDao;
import repository.ExchangeRateDao;
import service.CurrenciesService;
import service.CurrencyService;
import service.ExchangeRateService;
import util.JsonMapper;


@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HikariDataSource dataSource = getDataSource();
        ServletContext context = sce.getServletContext();

        context.setAttribute("dataSource", dataSource);

        CurrenciesDao currenciesDao = new CurrenciesDao(dataSource);
        CurrencyDao currencyDao = new CurrencyDao(dataSource);
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao(dataSource, currenciesDao);

        CurrenciesService currenciesService = new CurrenciesService(currenciesDao);
        CurrencyService currencyService = new CurrencyService(currencyDao);
        ExchangeRateService exchangeRatesService = new ExchangeRateService(exchangeRateDao);
        JsonMapper mapper = new JsonMapper();

        context.setAttribute("currenciesService", currenciesService);
        context.setAttribute("currencyService", currencyService);
        context.setAttribute("exchangeRatesService", exchangeRatesService);
        context.setAttribute("jsonMapper", mapper);
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
