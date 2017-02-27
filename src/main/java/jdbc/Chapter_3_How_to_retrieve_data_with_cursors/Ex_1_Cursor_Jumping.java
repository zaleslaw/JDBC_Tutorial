package jdbc.Chapter_3_How_to_retrieve_data_with_cursors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This example demonstrates strange behavior of TYPE_FORWARD_ONLY cursor with JDBC-MySQL driver
 * Yes, you can scroll it in forward and backward directions
 */
public class Ex_1_Cursor_Jumping extends Connectable {

    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT * FROM taxi_ride",
                     ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {

            ResultSet rs = st.executeQuery();
            rs.first();
            log.info("First " + rs.getRow() + ". " + rs.getString("start_point")
                    + "\t" + rs.getString("end_point"));
            rs.last();
            log.info("Last " + rs.getRow() + ". " + rs.getString("start_point")
                    + "\t" + rs.getString("end_point"));

            rs.afterLast();


            // Unbelievable, but it works
            log.info("DESC ORDER");
            while (rs.previous()) {
                log.info(rs.getRow() + ". " + rs.getString("start_point")
                        + "\t" + rs.getString("end_point"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
