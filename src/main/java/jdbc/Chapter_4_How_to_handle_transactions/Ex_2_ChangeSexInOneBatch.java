package jdbc.Chapter_4_How_to_handle_transactions;

import jdbc.Chapter_3_How_to_retrieve_data_with_cursors.Connectable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Batch technique is very useful then you need deliver all statements together through the network
 * Don't forget to enable batch support for your Database
 * <p>
 * This example help to revert change sex in backward direction
 * </p>
 */
public class Ex_2_ChangeSexInOneBatch extends Connectable {

    public static final String FEMALE = "F";
    public static final String MALE = "M";

    public static void main(String[] args) {

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT * FROM driver WHERE sex = ?");
             PreparedStatement updateSt = connection.prepareStatement("UPDATE driver SET sex = ? WHERE id = ?")) {

            // SELECT ALL MALES
            st.setString(1, MALE);
            ResultSet rs = st.executeQuery();
            log.info("Men List");
            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString("firstname") + "\t" + rs.getString("lastname"));
            }

            // UPDATE SEX FOR ONE MAN AND ONE WOMAN
            updateSt.setString(1, FEMALE);
            updateSt.setInt(2, 2);
            updateSt.addBatch();


            updateSt.setString(1, MALE);
            updateSt.setInt(2, 3);
            updateSt.addBatch();

            final int[] ints = updateSt.executeBatch();
            log.info("Batch results: " + Arrays.toString(ints));

            log.info("Sex was exchanged");

            // SELECT ALL FEMALES
            st.setString(1, FEMALE);
            rs = st.executeQuery();
            log.info("Women List");
            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString("firstname") + "\t" + rs.getString("lastname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
