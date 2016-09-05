package jdbc.statements;

import java.sql.*;
import java.util.Random;

/**
 * Very often we are trying to parametrize our SQL queries
 * Please use PreparedStatement for that
 */
public class Ex_6_PreparedStatement {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection(DB_NAME); PreparedStatement st = connection.prepareStatement("SELECT * FROM customers WHERE sex = ?")) { // Prepare Statement
            for (int i = 0; i < 5; i++) {
                System.out.println("Round #" + i);
                st.setString(1, new Random().nextBoolean() ? "male" : "female"); // Randomize our choice
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getRow() + " " + rs.getString(2) + " " + rs.getDate("birthdate").toLocalDate().getYear());
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
