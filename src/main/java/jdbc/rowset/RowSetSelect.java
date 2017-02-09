package rowset;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.SQLException;

/**
 * Created by Alexey_Zinovyev on 07-Sep-16.
 */
public class RowSetSelect {

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
            while (rowSet.next()) {
                System.out.println(rowSet.getRow() + " " + rowSet.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
