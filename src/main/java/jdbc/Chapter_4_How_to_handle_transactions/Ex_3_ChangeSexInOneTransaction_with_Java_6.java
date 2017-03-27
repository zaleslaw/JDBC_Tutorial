package jdbc.Chapter_4_How_to_handle_transactions;

import jdbc.Chapter_3_How_to_retrieve_data_with_cursors.Connectable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Sometimes we need change our sex together ...
 * In one transaction only
 */
public class Ex_3_ChangeSexInOneTransaction_with_Java_6 extends Connectable {

    public static final String FEMALE = "F";
    public static final String MALE = "M";

    public static void main(String[] args) {

        Connection connection = null;
        PreparedStatement st = null;
        PreparedStatement updateSt = null;

        try {

            connection = getConnection();
            st = connection.prepareStatement("SELECT * FROM customers WHERE sex = ?");
            updateSt = connection.prepareStatement("UPDATE customers SET sex = ? WHERE id = ?");

            // SELECT ALL MALES
            st.setString(1, MALE);
            ResultSet rs = st.executeQuery();
            System.out.println("Men List");
            while (rs.next()) {
                System.out.println(rs.getRow() + ". " + rs.getString("firstname")
                        + "\t" + rs.getString("lastname"));
            }

            // UPDATE SEX FOR ONE MAN AND ONE WOMAN
            connection.setAutoCommit(false); //<---------- START TRANSACTION
            updateSt.setString(1, FEMALE);
            updateSt.setInt(2, 3);
            updateSt.executeUpdate();


            // This code emulates broken transaction
            if (new Random().nextBoolean()) { // Sometimes shit happens, if you need it everytime use if(true)
                throw new SQLException("Database was broken");
            }

            updateSt.setString(1, MALE);
            updateSt.setInt(2, 2);
            updateSt.executeUpdate();

            log.info("Sex was exchanged");

            // SELECT ALL FEMALES
            st.setString(1, FEMALE);
            rs = st.executeQuery();
            log.info("Women List");
            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString("firstname") + "\t" + rs.getString("lastname"));
            }
            connection.commit(); //<------------ END TRANSACTION

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
