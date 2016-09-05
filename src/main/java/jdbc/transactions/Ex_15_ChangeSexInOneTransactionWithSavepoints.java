package jdbc.transactions;

import java.sql.*;
import java.util.Random;

/**
 * Let's return to Java 6 when we have deal with savepoints and jdbc.transactions
 */
public class Ex_15_ChangeSexInOneTransactionWithSavepoints {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";

    public static void main(String[] args) {

        Connection connection = null;
        PreparedStatement st = null;
        PreparedStatement updateSt = null;
        Savepoint savepoint = null;

        try {
            connection = getConnection(DB_NAME);
            st = connection.prepareStatement("SELECT * FROM customers WHERE sex = ?");
            updateSt = connection.prepareStatement("UPDATE customers SET sex = ? WHERE id = ?");

            connection.setAutoCommit(false);

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

            savepoint = connection.setSavepoint("New_Female_was_born");

            if (new Random().nextBoolean()) { /* Sometimes shit happens */
                throw new SQLException("SQL was brokeeeeen");
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


            connection.commit(); //<------------ END TRANSACTION

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback(savepoint);
                    /*
                   But first part of transaction (before savepoint) wasn't applied
                   Solution: call connection.commit()
                   */
                    //connection.commit();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
