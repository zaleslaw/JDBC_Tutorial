package jdbc.Chapter_1_How_to_connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Run program and catch MySQLNonTransientConnectionException with "Ex_3_TooManyConnections"
 * (Tune your database and set small size of Connection pool)
 * After that run with -Xmx10m and you will not catch Exception
 * What's happened with small size of heap?
 * How to fix?
 */
public class Ex_3_TooManyConnections {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws SQLException {
        for (int i = 0; i < 1_000; i++) {
            createAndDropTable();
        }
    }

    private static void createAndDropTable() throws SQLException {
        Connection connection = getConnection(DB_NAME);
        Statement st = connection.createStatement();

        st.execute("CREATE TABLE IF NOT EXISTS users (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(30), PRIMARY KEY (id))");
        System.out.println("Table 'users' was created");

        st.execute("DROP TABLE users");
        System.out.println("Table 'users' was dropped");

        /* Solution*/
        //connection.close();
    } // GC for local variables

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
