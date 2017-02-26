package jdbc.Chapter_2_How_to_extract_data_with_statements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * [NOTE] Before execution: Add stored procedure 'getAllShifts' to your MySQL database (Wrap SQL query 'SELECT * FROM shifts')
 * <p>
 * CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllShifts`()
 * <br>
 * BEGIN
 * <br>
 * SELECT * FROM shift;
 * <br>
 * END
 * </p>
 * <p>
 * This class calls stored procedure to print out all shifts from database
 */
public class Ex_5_StoredProcedure {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";

    public static void main(String[] args) {

        Logger log = LogManager.getRootLogger();

        try (Connection connection = getConnection();
             CallableStatement st = connection.prepareCall("CALL getAllShifts")) { // Call StoredProcedure getAllGoods with body: SELECT * FROM guber.shifts;

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                log.info(rs.getRow() + " " + rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
