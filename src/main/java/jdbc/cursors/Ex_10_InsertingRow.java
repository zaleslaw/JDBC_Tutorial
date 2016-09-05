package jdbc.cursors;

import java.sql.*;

/**
 * This example contains an error in PreparedStatement initialization
 * Fix it, choose correct parameter
 * <p>
 * Also you are able to insert row in the last position using .insertRow()
 */
public class Ex_10_InsertingRow {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection(DB_NAME);
             PreparedStatement st = connection.prepareStatement("SELECT * FROM products",
                     ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) { //Solution: FIX and change to ResultSet.CONCUR_UPDATEBABLE


            ResultSet rs = st.executeQuery();
            System.out.println("DESC ORDER");
            rs.last();
            rs.moveToInsertRow();
            rs.updateInt(1, 10);
            rs.updateString(2, "Newest Toy");
            rs.updateInt(3, 888);
            rs.insertRow();

            rs.beforeFirst();
            while (rs.next()) {
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
