package jdbc.Chapter_5_How_to_ETL_data_with_RowSet;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.SQLException;

/**
 * Also, you can subscribe on next events like .rowSetChanged/.rowChanged/.cursorMoved
 */
public class Ex_18_RowSetSelectWithListener {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) throws SQLException {

        try {
            JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();
            rowSet.setUrl(URL + DB_NAME);
            rowSet.setPassword(PASSWORD);
            rowSet.setUsername(USER_NAME);
            rowSet.setCommand("SELECT * FROM customers WHERE sex = ?");
            rowSet.setString(1, "male");
            rowSet.execute();

            rowSet.addRowSetListener(new RowSetListener() {
                @Override
                public void rowSetChanged(RowSetEvent event) {
                    System.out.println("RowSet was changed");
                }

                @Override
                public void rowChanged(RowSetEvent event) {
                    System.out.println("Row was changed");
                }

                @Override
                public void cursorMoved(RowSetEvent event) {
                    System.out.println("Cursor was moved");
                }
            });
            while (rowSet.next()) {
                System.out.println(rowSet.getRow() + " " + rowSet.getString(2));
            }

            rowSet.setCommand("SELECT * FROM prices");
            rowSet.execute();
            rowSet.last();
            rowSet.deleteRow();
            while (rowSet.previous()) {
                System.out.println(rowSet.getRow() + " " + rowSet.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
