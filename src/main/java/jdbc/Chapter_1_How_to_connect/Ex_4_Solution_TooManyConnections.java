package jdbc.Chapter_1_How_to_connect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Run program and you don't catch com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: Data source rejected establishment of connection
 */
public class Ex_4_Solution_TooManyConnections {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) {
        Logger log = LogManager.getRootLogger();
        try {

            for (int i = 0; i < 1_000; i++) {
                Connection connection = getConnection();
                Statement st = connection.createStatement();

                st.execute("CREATE TABLE IF NOT EXISTS users (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(30), PRIMARY KEY (id))");

                st.execute("DROP TABLE users");
                log.info("Connection # " + i + " was created");
                connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }

}
