package ru.vkokourov.repository;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CurrencyRepository {

    public static final String SELECT_ALL = "SELECT * FROM currencies";

    private Connection connection;

    public CurrencyRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:currency_exchange.db");
        } catch (ClassNotFoundException e) {
            log.debug("Can't get class. No driver found");
            e.printStackTrace();
        } catch (SQLException e) {
            log.debug("Can't get connection. Incorrect URL");
            e.printStackTrace();
        }
    }

    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();

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
        }

        return currencies;
    }
}
