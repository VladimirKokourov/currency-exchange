package ru.vkokourov.web;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.model.Currency;
import ru.vkokourov.service.CurrencyService;
import ru.vkokourov.util.JsonUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private CurrencyService service;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        service = (CurrencyService) ctx.getAttribute("CurrencyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().replace("/", "").toUpperCase();
        log.info("Get currency {} from Servlet", code);
        Currency currency = service.getByCode(code);
        resp.getWriter().write(JsonUtil.writeJson(currency));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
