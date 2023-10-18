package ru.vkokourov.web;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.model.ExchangeRate;
import ru.vkokourov.service.ExchangeRateService;
import ru.vkokourov.util.JsonUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@Slf4j
@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private ExchangeRateService service;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        service = (ExchangeRateService) ctx.getAttribute("ExchangeRateService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Get all exchange rates from Servlet");
        List<ExchangeRate> currencies = service.getAll();
        resp.getWriter().write(JsonUtil.writeJson(currencies));
        resp.setStatus(SC_OK);
    }
}
