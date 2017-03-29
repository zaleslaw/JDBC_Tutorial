package connectionPooling.demo;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.*;

/**
 * It doesn't work without magic from PoolableConnectionFactory
 */
public class DBCP_1_4_power {


    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws InterruptedException {


        GenericObjectPool connectionPool = new GenericObjectPool();
        connectionPool.setMaxActive(10);

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(URL, USER_NAME, PASSWORD);
        PoolableConnectionFactory poolableConnectionFactory
                = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);

        DataSource dataSource = new PoolingDataSource(connectionPool);

        System.out.println("Max active " + connectionPool.getMaxActive());
        System.out.println("Lifo " + connectionPool.getLifo());
        System.out.println("Num idle " + connectionPool.getNumIdle());

        while (true) {
            try (Connection connection = dataSource.getConnection(); Statement st = connection.createStatement()) {

                ResultSet rs = st.executeQuery("SELECT * FROM driver WHERE lastname LIKE 'De%'");
                while (rs.next()) {
                    System.out.println(rs.getRow() + " " + rs.getString(2) + " " + rs.getString("lastname") + " " + rs.getDate("birthdate").toLocalDate().getYear());
                }

                //Thread.sleep(10000);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
