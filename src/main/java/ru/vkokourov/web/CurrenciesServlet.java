package ru.vkokourov.web;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.exception.InvalidDataException;
import ru.vkokourov.model.Currency;
import ru.vkokourov.repository.CurrencyRepository;
import ru.vkokourov.service.CurrencyService;
import ru.vkokourov.util.ConnectionPool;
import ru.vkokourov.util.JsonUtil;
import ru.vkokourov.util.ValidationUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@WebServlet("/currencies")
@Slf4j
public class CurrenciesServlet extends HttpServlet {

    private CurrencyService service;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        service = (CurrencyService) ctx.getAttribute("CurrencyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Get all currencies from Servlet");
        List<Currency> currencies = service.getAll();
        resp.getWriter().write(JsonUtil.writeJson(currencies));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Post currency from Servlet");
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (ValidationUtil.isParameterForCurrencyMissing(parameterMap)) {
            throw new InvalidDataException("Отсутствует нужное поле формы");
        }
        if (!ValidationUtil.isParameterMapForCurrencyValid(parameterMap)) {
            throw new InvalidDataException("Некорректные данные");
        }
        Currency currency = new Currency();
        currency.setCode(parameterMap.get("code")[0]);
        currency.setFullName(parameterMap.get("name")[0]);
        currency.setSign(parameterMap.get("sign")[0]);

        resp.getWriter().write(JsonUtil.writeJson(service.save(currency)));
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
