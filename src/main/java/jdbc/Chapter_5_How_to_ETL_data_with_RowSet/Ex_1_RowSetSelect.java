package jdbc.Chapter_5_How_to_ETL_data_with_RowSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.SQLException;

/**
 * RowSet is a new word (since Java 5.0) in light-weight configuration of JDBC connection
 */
public class Ex_1_RowSetSelect {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";
    public static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws SQLException {

        try {
            JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();
            rowSet.setUrl(URL);
            rowSet.setPassword(PASSWORD);
            rowSet.setUsername(USER_NAME);
            rowSet.setCommand("SELECT * FROM cab WHERE has_baby_chair = ?");
            rowSet.setBoolean(1, true);
            rowSet.execute();
            while (rowSet.next()) {
                log.info(rowSet.getRow() + " " + rowSet.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
