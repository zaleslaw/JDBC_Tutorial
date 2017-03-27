package jdbc.Chapter_4_How_to_handle_transactions;

import jdbc.Chapter_3_How_to_retrieve_data_with_cursors.Connectable;

import java.sql.*;

/**
 * Of course, you should commit it
 */
public class Ex_6_Solution_ChangeSexInOneTransactionWithSavepoints extends Connectable {

    public static final String FEMALE = "F";
    public static final String MALE = "M";

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false); //<---------- START TRANSACTION
            Savepoint savepoint = null;
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
                connection.setAutoCommit(false); //<---------- START TRANSACTION
                updateSt.setString(1, FEMALE);
                updateSt.setInt(2, 3);
                updateSt.executeUpdate();

                savepoint = connection.setSavepoint("New_Female_was_born");
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
                e.printStackTrace();
                if (connection != null && savepoint != null) {
                    try {
                        connection.rollback(savepoint);
                        connection.commit();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
