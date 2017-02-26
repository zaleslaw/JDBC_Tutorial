package jdbc.Chapter_2_How_to_extract_data_with_statements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Full handling of SQL checked exception in Java 6
 * <ul>
 *     <li>Firstly, delete 'throws SQLException' in main() method signature</li>
 *     <li>Secondly, add null check and call of .close() method for ResultSet, Statement, Connection</li>
 * </ul>
 */
public class Ex_1_Select_Drivers_with_Java_6 {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws SQLException //Task1: Drop throws section
    {
        Logger log = LogManager.getRootLogger();

        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;

        /*try {*/
        connection = getConnection();
        st = connection.createStatement();

        rs = st.executeQuery("SELECT * FROM driver WHERE lastname LIKE 'De%'");
        while (rs.next()) {
            log.info(rs.getRow() + " " + rs.getString(2) + " " + rs.getString("lastname") + " " + rs.getDate("birthdate").toLocalDate().getYear());
        }
        //Solution: Don't forget required handling of null/SQLExceptions
        /*} catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs!=null){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(st!=null){
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/


    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
