package jdbc.Chapter_2_How_to_extract_data_with_statements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Task: Connection should be achievable in finally
 * Solution: Global reference:)
 */
public class Ex_3_Solution_Connection_in_finally {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";
    public static Connection globalConnection = null; //Let's make ref on connection


    public static void main(String[] args) throws SQLException {

        Logger log = LogManager.getRootLogger();

        try (Connection connection = getConnection(); Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM driver WHERE lastname LIKE 'De%'");
            while (rs.next()) {
                log.info(rs.getRow() + " " + rs.getString(2) + " " + rs.getString("lastname") + " " + rs.getDate("birthdate").toLocalDate().getYear());
            }
            globalConnection = connection;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println(globalConnection.getCatalog());
        }

    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}