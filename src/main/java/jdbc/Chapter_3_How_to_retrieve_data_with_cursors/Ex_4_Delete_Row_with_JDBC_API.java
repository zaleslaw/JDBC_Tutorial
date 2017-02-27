package jdbc.Chapter_3_How_to_retrieve_data_with_cursors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Also you are able to insert row in the last position using .insertRow()
 */
public class Ex_4_Delete_Row_with_JDBC_API extends Connectable {


    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT * FROM taxi_company", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = st.executeQuery()) {

            rs.afterLast();
            while (rs.previous()) {
                String name = rs.getString(2);
                if (name.equals("NETT")) {
                    rs.deleteRow();
                }

            }

            rs.beforeFirst();
            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString(2) + "\t" + rs.getString(4));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
