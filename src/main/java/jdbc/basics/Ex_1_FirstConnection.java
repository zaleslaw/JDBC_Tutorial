package jdbc.basics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Let's create connection according MySQL URL Syntax
 * MySql URL syntax
 * jdbc:mysql://[host][,failoverhost...[:port]/[database][?propertyName1][=propertyValue1]
 * [&propertyName2][=propertyValue2]
 */
public class Ex_1_FirstConnection {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";

    public static void main(String[] args) throws SQLException {
        Connection connection = getConnection(DB_NAME);
        System.out.println(connection.getCatalog());
    }

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }

}
