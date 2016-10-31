package jdbc.statements;

import java.sql.*;

/**
 * If you are really like checked exceptions
 * Try to drop throws SQLException in main() method
 */
public class Ex_4_SelectCustomers {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws SQLException //Task1: Drop throws section
    {

        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;

        /*try {*/
        connection = getConnection(DB_NAME);
        st = connection.createStatement();

        rs = st.executeQuery("SELECT * FROM customers WHERE sex = 'male'");
        while (rs.next()) {
            System.out.println(rs.getRow() + " " + rs.getString(2) + " " + rs.getDate("birthdate").toLocalDate().getYear());
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

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
