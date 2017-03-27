package jdbc.Chapter_4_How_to_handle_transactions;

import jdbc.Chapter_3_How_to_retrieve_data_with_cursors.Connectable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Possible solution: define boundary lines of transaction correctly
 */
public class Ex_5_Solution_Incorrect_table_name extends Connectable {

    public static final String FEMALE = "F";
    public static final String MALE = "M";

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false); //<---------- START TRANSACTION
            try (PreparedStatement st = connection.prepareStatement("SELECT * FROM driver WHERE sex = ?");
                 PreparedStatement updateSt = connection.prepareStatement("UPDATE driver SET sex = ? WHERE id = ?")) {

                // SELECT ALL MALES
                st.setString(1, MALE);
                ResultSet rs = st.executeQuery();
                log.info("Men List");
                while (rs.next()) {
                    log.info(rs.getRow() + ". " + rs.getString("firstname") + "\t" + rs.getString("lastname"));
                }

                // UPDATE SEX FOR ONE MAN AND ONE WOMAN
                //connection.setAutoCommit(false); //<---------- START TRANSACTION
                updateSt.setString(1, FEMALE);
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

            } catch (SQLException e) {
            /* Trying to rollback transaction */
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.info("Second catch clause");
            e.printStackTrace();
        }
    }
}
