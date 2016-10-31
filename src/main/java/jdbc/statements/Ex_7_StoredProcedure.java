package jdbc.statements;

import java.sql.*;

/**
 * [NOTE] Before execution: Add stored procedure 'getAllGoods' to your MySQL database (Wrap SQL query 'SELECT * FROM products')
 *
 * This class calls stored procedure to print out all products from database
 *
 */
public class Ex_7_StoredProcedure {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";

    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection(DB_NAME);
             CallableStatement st = connection.prepareCall("CALL getAllGoods")) { // Call StoredProcedure getAllGoods with body: SELECT * FROM shop.customers;

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getRow() + " " + rs.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
