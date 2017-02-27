package jdbc.Chapter_5_How_to_ETL_data_with_RowSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.SQLException;

/**
 * Also, you can subscribe on next events like .rowSetChanged/.rowChanged/.cursorMoved
 */
public class Ex_2_RowSet_CRUD_With_Listeners {

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";
    public static final Logger log = LogManager.getRootLogger();


    public static void main(String[] args) {

        try {
            JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();
            rowSet.setUrl(URL);
            rowSet.setPassword(PASSWORD);
            rowSet.setUsername(USER_NAME);
            rowSet.setCommand("SELECT * FROM cab WHERE has_baby_chair = ?");
            rowSet.setBoolean(1, true);
            rowSet.execute();

            rowSet.addRowSetListener(new RowSetListener() {
                @Override
                public void rowSetChanged(RowSetEvent event) {
                    log.info("RowSet was changed");
                }

                @Override
                public void rowChanged(RowSetEvent event) {
                    log.info("Row was changed");
                }

                @Override
                public void cursorMoved(RowSetEvent event) {
                    log.info("Cursor was moved");
                }
            });
            while (rowSet.next()) {
                log.info(rowSet.getRow() + " " + rowSet.getString(2));
            }


            rowSet.setCommand("SELECT * FROM taxi_company");
            rowSet.execute();

            rowSet.moveToInsertRow();
            rowSet.updateInt(1, 4);
            rowSet.updateString(2, "NETT");
            rowSet.updateString(3, "Default City");
            rowSet.updateInt(4, 8888);
            rowSet.insertRow();

            rowSet.afterLast();
            while (rowSet.previous()) {
                log.info(rowSet.getRow() + " " + rowSet.getString(2));
            }

            // Deletion of NETT taxi
            rowSet.last();
            rowSet.deleteRow();

            // Print out 3 taxi company
            rowSet.beforeFirst();
            while (rowSet.next()) {
                log.info(rowSet.getRow() + " " + rowSet.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
