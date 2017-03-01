package logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * TODO: Move to log4j2 http://javatalks.ru/topics/43179?page=1#220253
 * http://baxincc.cc/questions/1513526/how-to-log-sql-with-log4jdbc
 * Additional options http://kveeresham.blogspot.ru/2015/03/logging-jdbc-activities-using-log4jdbc.html
 */
public class Ex_4_Solution_Parametrized_Query {

    public static final String URL = "jdbc:log4jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws ClassNotFoundException {

        Logger log = LogManager.getRootLogger();
        Class.forName("net.sf.log4jdbc.DriverSpy");

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
