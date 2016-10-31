package jdbc.statements;

import java.sql.*;

/**
 * It's time to go to Java 7. Remove ugly catch section and go ahead!
 *
 * Task 1: connection should be achievable in finally
 * Solution: Global reference:)
 */
public class Ex_5_SelectCustomers_Java_7 {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";
    // public static Connection connPool = null; //Let's make ref on connection


    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection(DB_NAME); Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM customers WHERE sex = 'male'");
            while (rs.next()) {
                System.out.println(rs.getRow() + " " + rs.getString(2) + " " + rs.getDate("birthdate").toLocalDate().getYear());
            }
            // connPool = connection;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //System.out.println(connPool.getCatalog());
        }

    }

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
