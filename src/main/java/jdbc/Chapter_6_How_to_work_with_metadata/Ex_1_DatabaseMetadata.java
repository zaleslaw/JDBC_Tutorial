package jdbc.Chapter_6_How_to_work_with_metadata;

import jdbc.Chapter_3_How_to_retrieve_data_with_cursors.Connectable;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * With this example you can ship over tables and columns of selected database
 */
public class Ex_1_DatabaseMetadata extends Connectable {

    public static void main(String[] args) {

        Connection connection = null;
        ResultSet tables = null;

        try {
            connection = getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            log.info("DB " + dbMetaData.getDatabaseProductName()
                    + " with driver " + dbMetaData.getDriverName()
                    + " and max columns in GROUP BY " + dbMetaData.getMaxColumnsInGroupBy());


            tables = dbMetaData.getTables(null, null, null, null);

            while (tables.next()) {
                String tableName = tables.getString(3);
                log.info("Meta info about table " + tableName);

                ResultSet columns = dbMetaData.getColumns(null, null, tableName, null);
                while (columns.next()) {
                    String name = columns.getString("COLUMN_NAME");
                    String type = columns.getString("TYPE_NAME");
                    int size = columns.getInt("COLUMN_SIZE");

                    log.info("Column name: [" + name + "]; type: [" + type
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

}
