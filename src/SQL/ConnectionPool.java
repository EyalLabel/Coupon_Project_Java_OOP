package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

//mysql:mysql-connector-java:jar:8.0.25
public class ConnectionPool {

    private static ConnectionPool instance = null;
    public static final int NUM_OF_CONNECTIONS = 10;
    private Stack<Connection> connections = new Stack<>();

    public static ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    private ConnectionPool() throws SQLException {
        openAllConnections();
    }

    public void openAllConnections() throws SQLException {
        for (int index = 0; index < NUM_OF_CONNECTIONS; index += 1) {
            Connection connection = DriverManager.getConnection(DataBaseManager.URL, DataBaseManager.USERNAME, DataBaseManager.PASSWORD);
            connections.push(connection);
        }
    }

    public Connection getConnection() throws InterruptedException {
        synchronized (connections) {
            if (connections.isEmpty()) {
                //wait until we will get a connection back
                connections.wait();
            }
            return connections.pop();
        }
    }

    public void returnConnection(Connection connection) {
        synchronized (connections) {
            connections.push(connection);
            //notify that we got back a connection from the user...
            //notify all connections that are waiting to connection to be release..
            connections.notify();
        }
    }

    public void closeAllConnection() throws InterruptedException {
        synchronized (connections) {
            while (connections.size() < NUM_OF_CONNECTIONS) {
                connections.wait();
            }
            connections.removeAllElements();
        }
    }
}
