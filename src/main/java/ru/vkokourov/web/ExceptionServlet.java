package ru.vkokourov.web;

import ru.vkokourov.exception.ApplicationException;
import ru.vkokourov.exception.ErrorMessage;
import ru.vkokourov.util.JsonUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exceptions")
public class ExceptionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processError(req, resp);
    }

    private void processError(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
        String message = throwable.getMessage();
        Integer statusCode = (Integer) req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (throwable instanceof ApplicationException) {
            ApplicationException ae = (ApplicationException) throwable;
            statusCode = ae.getStatusCode();
        }

        resp.setStatus(statusCode);
        resp.getWriter().write(JsonUtil.writeJson(new ErrorMessage(message)));
    }
}
