package jdbc.Chapter_1_How_to_connect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DDL operators (CREATE TABLE, DROP TABLE) are presented in this example.
 * Also we refactored connection establishment in separate method
 */
public class Ex_3_Create_And_Drop_Table {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) {
        Logger log = LogManager.getRootLogger();
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();

            st.execute("CREATE TABLE IF NOT EXISTS users (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(30), PRIMARY KEY (id))");
            log.info("Table 'users' was created");

            st.execute("DROP TABLE users");
            log.info("Table 'users' was dropped");


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }

}
