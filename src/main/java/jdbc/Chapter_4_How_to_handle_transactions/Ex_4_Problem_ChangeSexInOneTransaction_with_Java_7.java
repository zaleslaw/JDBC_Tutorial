package jdbc.Chapter_4_How_to_handle_transactions;

import jdbc.Chapter_3_How_to_retrieve_data_with_cursors.Connectable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * If use try-with-resources we will catch a problem in 'catch' section
 */
public class Ex_4_Problem_ChangeSexInOneTransaction_with_Java_7 extends Connectable {

    public static final String FEMALE = "F";
    public static final String MALE = "M";

    public static void main(String[] args) {

        try (Connection connection = getConnection();
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
            connection.setAutoCommit(false); //<---------- START TRANSACTION
            updateSt.setString(1, "F");
            updateSt.setInt(2, 3);
            updateSt.executeUpdate();


            // This code emulates broken transaction
            if (true) {
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

        } catch (Exception e) {
            e.printStackTrace();
            /* Trying to rollback transaction */
            //connection.rollback()

            /* According to the language spec,
             the connection will be closed before the catch clause is executed */
        }
    }
}
