package connectionPooling.demo;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * API in DBCP 2 was changed
 */
public class DBCP_2_1_power {


    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(URL, USER_NAME, PASSWORD);
        PoolableConnectionFactory poolFactory = new PoolableConnectionFactory(connectionFactory, null);
        GenericObjectPool connectionPool = new GenericObjectPool(poolFactory);
        poolFactory.setPool(connectionPool);
        DataSource dataSource = new PoolingDataSource(connectionPool);


        try (Connection connection = dataSource.getConnection(); Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM driver WHERE lastname LIKE 'De%'");
            while (rs.next()) {
                System.out.println(rs.getRow() + " " + rs.getString(2) + " " + rs.getString("lastname") + " " + rs.getDate("birthdate").toLocalDate().getYear());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }




    }
}
