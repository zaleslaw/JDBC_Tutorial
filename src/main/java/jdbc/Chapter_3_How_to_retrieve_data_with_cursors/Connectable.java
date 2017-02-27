package jdbc.Chapter_3_How_to_retrieve_data_with_cursors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Base class for all examples
 */
public class Connectable {

    public static final Logger log = LogManager.getRootLogger();

    public static Connection getConnection() throws SQLException {

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
