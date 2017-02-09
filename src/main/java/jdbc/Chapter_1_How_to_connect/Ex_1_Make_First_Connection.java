package jdbc.Chapter_1_How_to_connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Let's create connection according MySQL URL Syntax
 * <p>
 * jdbc:mysql://[host][,failoverhost...[:port]/[database][?propertyName1][=propertyValue1][&propertyName2][=propertyValue2]
 */
public class Ex_1_Make_First_Connection {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";

    public static void main(String[] args) {

        // First approach
        try {
            Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Second approach
        try {
            Connection connection = DriverManager
                    .getConnection(URL + "?user=" + USER_NAME + "&password=" + PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Third approach
        Properties props = new Properties();
        props.setProperty("user", USER_NAME);
        props.setProperty("password", PASSWORD);
        try {
            Connection connection = DriverManager.getConnection(URL, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
