package SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DButils {
    public static void runQuery(String sql) throws SQLException {
        Connection connection = null;
        try {
            //take a connection from the connection pool.
            connection = ConnectionPool.getInstance().getConnection();
            //run the sql command
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();
        } catch (InterruptedException | SQLException e) {
            System.out.println("failed to run query");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }
}
