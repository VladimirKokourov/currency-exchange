package ru.vkokourov;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.repository.CurrencyRepository;
import ru.vkokourov.repository.ExchangeRateRepository;
import ru.vkokourov.service.CurrencyService;
import ru.vkokourov.service.ExchangeRateService;
import ru.vkokourov.util.ConnectionPool;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@Slf4j
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Servlet context initialized");
        ServletContext ctx = sce.getServletContext();

        String url = ctx.getInitParameter("DBURL");
        String driver = ctx.getInitParameter("DBDRIVER");
        log.info("Create connection pool");
        ConnectionPool connectionPool;
        try {
            connectionPool = ConnectionPool.create(url, driver);
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Ошибка подключения к БД");
            throw new RuntimeException(e);
        }

        CurrencyRepository currencyRepository = new CurrencyRepository(connectionPool);
        CurrencyService currencyService = new CurrencyService(currencyRepository);

        ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepository(connectionPool);
        ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateRepository);

        ctx.setAttribute("CurrencyService", currencyService);
        ctx.setAttribute("ExchangeRateService", exchangeRateService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Servlet context destroyed");
    }
}
