package ru.vkokourov.repository;

import lombok.extern.slf4j.Slf4j;
import ru.vkokourov.exception.AlreadyExistException;
import ru.vkokourov.exception.ApplicationException;
import ru.vkokourov.exception.NotExistException;
import ru.vkokourov.exception.ServerException;
import ru.vkokourov.model.Currency;
import ru.vkokourov.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CurrencyRepository {

    private static final String SELECT_ALL = "SELECT * FROM currencies";
    private static final String SELECT_BY_CODE = "SELECT * FROM currencies WHERE code=?";
    private static final String CREATE = "INSERT INTO currencies(code, full_name, sign) VALUES (?,?,?)";
    private static final String UPDATE = "UPDATE currencies SET code=?, full_name=?, sign=? WHERE id=?";
    private static final int ERROR_CODE_UNIQUE_CONSTRAINT_FAILED = 19;

    private final ConnectionPool connectionPool;

    public CurrencyRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<Currency> getAll() throws ApplicationException {
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

    public Currency getByCode(String code) throws ApplicationException {
        log.info("Get currency {} from repository", code);
        Currency currency = new Currency();
        Connection connection = connectionPool.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_CODE)) {
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

    public Currency create(Currency currency) throws ApplicationException {
        log.info("Create currency from repository. {}", currency);
        Connection connection = connectionPool.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(CREATE)) {
            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getFullName());
            stmt.setString(3, currency.getSign());
            int i = stmt.executeUpdate();
            log.info("number {}", i);
        } catch (SQLException e) {
            log.error(e.getMessage());
            if (e.getErrorCode() == ERROR_CODE_UNIQUE_CONSTRAINT_FAILED) {
                throw new AlreadyExistException("Валюта с таким кодом уже существует");
            } else {
                throw new ServerException("Ошибка записи в БД");
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }

        int id = getByCode(currency.getCode()).getId();
        currency.setId(id);

        return currency;
    }

    public void update(Currency currency) throws ApplicationException {
        log.info("Update currency from repository. {}", currency);
        Connection connection = connectionPool.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getFullName());
            stmt.setString(3, currency.getSign());
            stmt.setInt(4, currency.getId());
            if (stmt.executeUpdate() == 0) {
                log.error("Count update rows equals zero");
                throw new NotExistException("Валюты с id:" + currency.getId() + " не существует");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServerException("Ошибка записи в БД");
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private void fillCurrency(Currency currency, ResultSet rs) throws SQLException {
        currency.setId(rs.getInt("id"));
        currency.setCode(rs.getString("code"));
        currency.setFullName(rs.getString("full_name"));
        currency.setSign(rs.getString("sign"));
    }
}
