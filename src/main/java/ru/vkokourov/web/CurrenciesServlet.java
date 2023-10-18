package ru.vkokourov.web;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.exception.ApplicationException;
import ru.vkokourov.exception.ErrorMessage;
import ru.vkokourov.exception.InvalidDataException;
import ru.vkokourov.model.Currency;
import ru.vkokourov.service.CurrencyService;
import ru.vkokourov.util.JsonUtil;
import ru.vkokourov.util.ValidationUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.*;

@Slf4j
@WebServlet("/currencies")
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
        resp.setStatus(SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Post currency from Servlet");
        Currency currency = getCurrency(req);

        resp.getWriter().write(JsonUtil.writeJson(service.create(currency)));
        resp.setStatus(SC_CREATED);
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Update currency from servlet");
        Currency currency;
        try {
            currency = getCurrency(req);
            if (currency.getId() == 0) {
                resp.getWriter().write(JsonUtil.writeJson(service.create(currency)));
                resp.setStatus(SC_CREATED);
            } else {
                service.update(currency);
                resp.setStatus(SC_NO_CONTENT);
            }
        } catch (ApplicationException e) {
            // default ServletExceptions handling doesn't work in doPut, throws 405 error on any exception
            resp.setStatus(e.getStatusCode());
            resp.getWriter().write(JsonUtil.writeJson(new ErrorMessage(e.getMessage())));
        }
    }

    private Currency getCurrency(HttpServletRequest req) throws ApplicationException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (ValidationUtil.isParameterForCurrencyMissing(parameterMap)) {
            log.error("Miss parameter for currency");
            throw new InvalidDataException("Отсутствует нужное поле формы");
        }
        if (!ValidationUtil.isParameterMapForCurrencyValid(parameterMap)) {
            log.error("Invalid parameters");
            throw new InvalidDataException("Некорректные данные");
        }
        Currency currency = new Currency();
        currency.setId(Integer.parseInt(parameterMap.getOrDefault("id", new String[]{"0"})[0]));
        currency.setCode(parameterMap.get("code")[0]);
        currency.setFullName(parameterMap.get("name")[0]);
        currency.setSign(parameterMap.get("sign")[0]);

        return currency;
    }
}
