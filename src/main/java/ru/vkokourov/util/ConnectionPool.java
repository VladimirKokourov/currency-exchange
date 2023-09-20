package ru.vkokourov.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ConnectionPool {

    private static final int INITIAL_POOL_SIZE = 10;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();

    private ConnectionPool(List<Connection> connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static ConnectionPool create(String url, String driver) throws SQLException, ClassNotFoundException {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url, driver));
        }

        return new ConnectionPool(pool);
    }

    private static Connection createConnection(String url, String driver)
            throws SQLException, ClassNotFoundException {
        Class.forName(driver);

        return DriverManager.getConnection(url);
    }

    public Connection getConnection() {
        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);
        usedConnections.add(connection);

        return connection;
    }

    public void releaseConnection(Connection connection) {
        connectionPool.add(connection);
        usedConnections.remove(connection);
    }
}
