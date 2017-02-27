package jdbc.Chapter_3_How_to_retrieve_data_with_cursors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Also you are able to insert row in the last position using .insertRow()
 */
public class Ex_3_Insert_Row extends Connectable {


    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT * FROM taxi_company", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = st.executeQuery()) {


            log.info("DESC ORDER");
            rs.last();
            // Begin "INSERT" mode
            rs.moveToInsertRow();
            rs.updateInt(1, 4);
            rs.updateString(2, "NETT");
            rs.updateString(3, "Default City");
            rs.updateInt(4, 8888);
            rs.insertRow();
            // End "INSERT" mode

            rs.beforeFirst();
            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString(2) + "\t" + rs.getString(4));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
