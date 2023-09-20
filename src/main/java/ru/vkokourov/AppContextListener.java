package ru.vkokourov;

import lombok.extern.slf4j.Slf4j;
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
        try {
            ConnectionPool connectionPool = ConnectionPool.create(url, driver);
            ctx.setAttribute("ConnectionPool", connectionPool);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Servlet context destroyed");
    }
}
