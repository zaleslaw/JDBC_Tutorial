package jdbc.Chapter_3_How_to_retrieve_data_with_cursors;

import java.sql.*;

/**
 * This example demonstrates strange behavior of TYPE_FORWARD_ONLY cursor with JDBC-MySQL driver
 * Yes, you can scroll it
 */
public class Ex_8_CursorJumping {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection(DB_NAME);
             PreparedStatement st = connection.prepareStatement("SELECT * FROM products",
                     ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {


            ResultSet rs = st.executeQuery();
            rs.first();
            System.out.println("First " + rs.getRow() + ". " + rs.getString(2)
                    + "\t" + rs.getString(3));
            rs.last();
            System.out.println("Last " + rs.getRow() + ". " + rs.getString(2)
                    + "\t" + rs.getString(3));

            rs.afterLast();

            System.out.println("DESC ORDER");
            while (rs.previous()) {
                System.out.println(rs.getRow() + ". " + rs.getString(2)
                        + "\t" + rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
