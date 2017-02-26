package jdbc.Chapter_2_How_to_extract_data_with_statements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * It's time to go to Java 7. Remove ugly catch section and go ahead!
 */
public class Ex_2_SelectCustomers_with_Java_7 {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";

    public static void main(String[] args) {

        Logger log = LogManager.getRootLogger();

        try (Connection connection = getConnection(); Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM driver WHERE lastname LIKE 'De%'");
            while (rs.next()) {
                log.info(rs.getRow() + " " + rs.getString(2) + " " + rs.getString("lastname") + " " + rs.getDate("birthdate").toLocalDate().getYear());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
