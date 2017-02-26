package jdbc.Chapter_2_How_to_extract_data_with_statements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Very often we are trying to parametrize our SQL queries.
 * Please use PreparedStatement for that.
 * <p>
 * TOREAD: http://articles.javatalks.ru/articles/2
 * <p>
 * The MySQL JDBC Driver defines these two properties as:
 * <ul>
 * <li>useServerPrepStmts - Use server-side prepared statements if the server supports them</li>
 * <li>cachePrepStmts - Should the driver cache the parsing stage of PreparedStatements of client-side prepared statements, the "check" for suitability of server-side prepared and server-side prepared statements themselves</li>
 * </ul>
 */
public class Ex_4_Solution_Parametrized_Query {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) {

        Logger log = LogManager.getRootLogger();

        // making one pre-compiled query for all iterations
        try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement("SELECT * FROM cab WHERE capacity = ?")) {

            for (int i = 0; i < 30; i++) {
                log.info("Round #" + i);
                st.setInt(1, i); // Setting up int parameter in safe way
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    log.info(rs.getString("car_make"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
