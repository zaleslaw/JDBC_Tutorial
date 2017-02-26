package jdbc.Chapter_2_How_to_extract_data_with_statements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Let's print cab with changing capacity (1 seat, 2 seats, ... 24 seats)
 */
public class Ex_4_Problem_Parametrized_Query {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) {

        Logger log = LogManager.getRootLogger();

        try (Connection connection = getConnection(); Statement st = connection.createStatement()) {
            for (int i = 0; i < 30; i++) {
                log.info("Round #" + i);
                ResultSet rs = st.executeQuery("SELECT * FROM cab WHERE capacity = " + i); // new query for each iteration; i - unsafe paramenter
                while (rs.next()) {
                    log.info(rs.getString("car_make"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
