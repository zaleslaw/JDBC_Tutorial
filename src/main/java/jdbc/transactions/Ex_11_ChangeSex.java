package jdbc.transactions;

import java.sql.*;
import java.util.Random;

/**
 * The time to change sex has gone
 */
public class Ex_11_ChangeSex {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection(DB_NAME);
             PreparedStatement st = connection.prepareStatement("SELECT * FROM customers WHERE sex = ?");
             PreparedStatement updateSt = connection.prepareStatement("UPDATE customers SET sex = ? WHERE id = ?")) {

            // SELECT ALL MALES
            st.setString(1, "male");
            ResultSet rs = st.executeQuery();
            System.out.println("Men List");
            while (rs.next()) {
                System.out.println(rs.getRow() + ". " + rs.getString("firstname")
                        + "\t" + rs.getString("lastname"));
            }

            // UPDATE SEX FOR ONE MAN AND ONE WOMAN
            updateSt.setString(1, "female");
            updateSt.setInt(2, 1);
            updateSt.executeUpdate();

            if (new Random().nextBoolean()) { // Sometimes shit happens, if you need it everytime use if(true)
                throw new RuntimeException();
            }

            updateSt.setString(1, "male");
            updateSt.setInt(2, 3);
            updateSt.executeUpdate();

            System.out.println("Sex was exchanged");

            // SELECT ALL FEMALES
            st.setString(1, "female");
            rs = st.executeQuery();
            System.out.println("Women List");
            while (rs.next()) {
                System.out.println(rs.getRow() + ". " + rs.getString("firstname")
                        + "\t" + rs.getString("lastname"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
