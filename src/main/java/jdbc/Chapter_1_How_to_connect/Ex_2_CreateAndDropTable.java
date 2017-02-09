package jdbc.Chapter_1_How_to_connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * DDL operators are important too!
 */
public class Ex_2_CreateAndDropTable {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws SQLException {

        Connection connection = getConnection(DB_NAME);
        Statement st = connection.createStatement();

        st.execute("CREATE TABLE IF NOT EXISTS users (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(30), PRIMARY KEY (id))");
        System.out.println("Table 'users' was created");

        st.execute("DROP TABLE users");
        System.out.println("Table 'users' was dropped");


    }

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
