package ru.vkokourov.web;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.model.Currency;
import ru.vkokourov.repository.CurrencyRepository;
import ru.vkokourov.util.ConnectionPool;
import ru.vkokourov.util.JsonUtil;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
@Slf4j
public class CurrenciesServlet extends HttpServlet {

    private CurrencyRepository repository;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        ConnectionPool connectionPool = (ConnectionPool) ctx.getAttribute("ConnectionPool");
        repository = new CurrencyRepository(connectionPool);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("getAll()");
        List<Currency> currencies = repository.getAll();
        resp.getWriter().write(JsonUtil.writeJson(currencies));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
