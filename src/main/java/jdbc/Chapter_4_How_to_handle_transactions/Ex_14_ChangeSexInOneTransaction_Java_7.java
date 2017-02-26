package jdbc.Chapter_4_How_to_handle_transactions;

import java.sql.*;

/**
 * Of course, we are modern and sexy and can merge try-with-resources and transaction JDBC API
 */
public class Ex_14_ChangeSexInOneTransaction_Java_7 {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";

    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection(DB_NAME)) {
            //connection.setAutoCommit(false); //SOLUTION <---------- START TRANSACTION
            try (PreparedStatement st = connection.prepareStatement("SELECT * FROM customerss WHERE sex = ?");
                 //Solution "SELECT * FROM customers WHERE sex = ?"
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
                connection.setAutoCommit(false); //<---------- START TRANSACTION with Exception in rollback
                updateSt.setString(1, "female");
                updateSt.setInt(2, 1);
                updateSt.executeUpdate();

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


                connection.commit(); //<------------ END TRANSACTION

            } catch (SQLException e) {
            /* Trying to rollback transaction */
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("Second catch clause");
            e.printStackTrace();
        }
    }

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
