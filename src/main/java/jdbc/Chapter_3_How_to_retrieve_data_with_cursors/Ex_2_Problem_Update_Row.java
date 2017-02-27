package jdbc.Chapter_3_How_to_retrieve_data_with_cursors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Also you are able to update row in the last position using .insertRow()
 */
public class Ex_2_Problem_Update_Row extends Connectable {

    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT * FROM taxi_company ");
             ResultSet rs = st.executeQuery()) {
            log.info("DESC ORDER");
            rs.afterLast();
            while (rs.previous()) {
                int rate = rs.getInt(4);
                rs.updateInt(4, rate + 1);
                rs.updateRow();
            }

            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString(2) + "\t" + rs.getString(4));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
