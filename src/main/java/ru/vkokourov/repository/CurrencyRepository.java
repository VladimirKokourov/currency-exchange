package ru.vkokourov.repository;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.exception.ServerException;
import ru.vkokourov.model.Currency;
import ru.vkokourov.util.ConnectionPool;

import javax.servlet.ServletException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CurrencyRepository {

    public static final String SELECT_ALL = "SELECT * FROM currencies";

    private final ConnectionPool connectionPool;

    public CurrencyRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<Currency> getAll() throws ServletException {
        log.info("Get all currencies from repository");
        List<Currency> currencies = new ArrayList<>();
        Connection connection = connectionPool.getConnection();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                Currency currency = new Currency();
                fillCurrency(currency, rs);
                currencies.add(currency);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServerException("Ошибка чтения из БД");
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return currencies;
    }

    public Currency getByCode(String code) throws ServletException {
        log.info("Get currency {} from repository", code);
        Currency currency = new Currency();
        Connection connection = connectionPool.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM currencies WHERE code=?")) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            fillCurrency(currency, rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServerException("Ошибка чтения из БД");
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return currency;
    }

    private void fillCurrency(Currency currency, ResultSet rs) throws SQLException {
        currency.setId(rs.getInt("id"));
        currency.setCode(rs.getString("code"));
        currency.setFullName(rs.getString("full_name"));
        currency.setSign(rs.getString("sign"));
    }
}
