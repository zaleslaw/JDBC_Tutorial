package jdbc.Chapter_4_How_to_handle_transactions;

import java.sql.*;
import java.util.Arrays;

/**
 * Batch technique is very useful then you need deliver all statements together through the network
 * Don't forget to enable batch support for your Database
 */
public class Ex_12_ChangeSexInOneBatch {

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
            updateSt.addBatch();


            updateSt.setString(1, "male");
            updateSt.setInt(2, 3);
            updateSt.addBatch();


            updateSt.setString(1, "female");
            updateSt.setInt(2, 2);
            updateSt.addBatch();

            final int[] ints = updateSt.executeBatch();
            System.out.println("Batch results: " + Arrays.toString(ints));

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
