package ru.vkokourov.repository;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.exception.ApplicationException;
import ru.vkokourov.exception.ServerException;
import ru.vkokourov.model.Currency;
import ru.vkokourov.model.ExchangeRate;
import ru.vkokourov.util.ConnectionPool;

import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExchangeRateRepository {

    private static final String SELECT_ALL = "SELECT er.id AS erID,\n" +
            "       cb.id        AS cbId,\n" +
            "       cb.code      AS cbCode,\n" +
            "       cb.full_name AS cbName,\n" +
            "       cb.sign      AS cbSign,\n" +
            "       ct.id        AS ctId,\n" +
            "       ct.code      AS ctCode,\n" +
            "       ct.full_name AS ctName,\n" +
            "       ct.sign      AS ctSign,\n" +
            "       er.rate      AS rate\n" +
            "FROM exchange_rates er\n" +
            "         LEFT JOIN currencies cb ON er.base_currency_id = cb.id\n" +
            "         LEFT JOIN currencies ct ON er.target_currency_id = ct.id";

    private final ConnectionPool connectionPool;

    public ExchangeRateRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<ExchangeRate> getAll() throws ApplicationException {
        log.info("Get all exchange rates from repository");
        List<ExchangeRate> rates = new ArrayList<>();
        Connection connection = connectionPool.getConnection();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                ExchangeRate rate = new ExchangeRate();
                fillExchangeRate(rate, rs);
                rates.add(rate);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServerException("Ошибка чтения из БД");
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return rates;
    }

    private void fillExchangeRate(ExchangeRate rate, ResultSet rs) throws SQLException {
        Currency baseCurrency = new Currency();
        Currency targetCurrency = new Currency();

        baseCurrency.setId(rs.getInt("cbId"));
        baseCurrency.setCode(rs.getString("cbCode"));
        baseCurrency.setFullName(rs.getString("cbName"));
        baseCurrency.setSign(rs.getString("cbSign"));
        System.out.println(baseCurrency);

        targetCurrency.setId(rs.getInt("ctId"));
        targetCurrency.setCode(rs.getString("ctCode"));
        targetCurrency.setFullName(rs.getString("ctName"));
        targetCurrency.setSign(rs.getString("ctSign"));
        System.out.println(targetCurrency);

        rate.setId(rs.getInt("erId"));
        rate.setBaseCurrency(baseCurrency);
        rate.setTargetCurrency(targetCurrency);
        rate.setRate(rs.getBigDecimal("rate").round(new MathContext(6, RoundingMode.HALF_EVEN)));
    }


}
