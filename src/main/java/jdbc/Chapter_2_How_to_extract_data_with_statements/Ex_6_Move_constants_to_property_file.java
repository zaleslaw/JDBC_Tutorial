package jdbc.Chapter_2_How_to_extract_data_with_statements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * It's time to go to Java 7. Remove ugly catch section and go ahead!
 */
public class Ex_6_Move_constants_to_property_file {

    public static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) {

        try (Connection connection = getConnection(); Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM driver WHERE lastname LIKE 'De%'");
            while (rs.next()) {
                log.info(rs.getRow() + " " + rs.getString(2) + " " + rs.getString("lastname") + " " + rs.getDate("birthdate").toLocalDate().getYear());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static Connection getConnection() throws SQLException {

        Properties prop = new Properties();

        try (InputStream input = new FileInputStream("src/main/resources/jdbc.properties");) {
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("File not found " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return DriverManager.getConnection(
                prop.getProperty("jdbc.url"),
                prop.getProperty("jdbc.username"),
                prop.getProperty("jdbc.password")
        );
    }
}
