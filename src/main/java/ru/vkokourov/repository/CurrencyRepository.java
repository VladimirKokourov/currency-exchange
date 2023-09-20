package ru.vkokourov.repository;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.model.Currency;
import ru.vkokourov.util.ConnectionPool;

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

    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();
        Connection connection = connectionPool.getConnection();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                Currency currency = new Currency();
                currency.setId(rs.getInt("id"));
                currency.setCode(rs.getString("code"));
                currency.setFullName(rs.getString("full_name"));
                currency.setSign(rs.getString("sign"));
                currencies.add(currency);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return currencies;
    }
}
