package jdbc.Chapter_3_How_to_retrieve_data_with_cursors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This example lights removing with SQL
 */
public class Ex_5_Problem_Delete_Row_with_SQL extends Connectable {

    public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("DELETE FROM taxi_company WHERE name = 'NETT'");
             PreparedStatement selectSt = connection.prepareStatement("SELECT * FROM taxi_company");
             ResultSet rs = selectSt.executeQuery()
        ) {
            int result = st.executeUpdate();
            log.info(result + " rows were deleted");

            // Deleted row will be printed. What a Terrible Failure?
            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString(2) + "\t" + rs.getString(4));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
