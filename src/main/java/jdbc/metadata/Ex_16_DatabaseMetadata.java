package jdbc.metadata;

import java.sql.*;

/**
 * WIth this example you can ship over tables and columns of selected database
 */
public class Ex_16_DatabaseMetadata {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "shop";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";


    public static void main(String[] args) {

        Connection connection = null;
        ResultSet tables = null;

        try {
            connection = getConnection(DB_NAME);
            DatabaseMetaData dbMetaData = connection.getMetaData();
            System.out.println("DB " + dbMetaData.getDatabaseProductName()
                    + " with driver " + dbMetaData.getDriverName()
                    + " and max columns in GROUP BY " + dbMetaData.getMaxColumnsInGroupBy());


            tables = dbMetaData.getTables(null, null, null, null);

            while (tables.next()) {
                String tableName = tables.getString(3);
                System.out.println("Meta info about table " + tableName);

                ResultSet columns = dbMetaData.getColumns(null, null, tableName, null);
                while (columns.next()) {
                    String name = columns.getString("COLUMN_NAME");
                    String type = columns.getString("TYPE_NAME");
                    int size = columns.getInt("COLUMN_SIZE");

                    System.out.println("Column name: [" + name + "]; type: [" + type
                            + "]; size: [" + size + "]");
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (tables != null) {
                    tables.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Connection getConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(URL + databaseName, USER_NAME, PASSWORD);
    }
}
