package mock.dbunit;

import jdbc.Chapter_3_How_to_retrieve_data_with_cursors.Connectable;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.FileOutputStream;

/**
 * Creates simple dataset to test something
 */
public class DatabaseExportToXML extends Connectable{
        public static void main(String[] args) throws Exception
        {
            // database connection

            IDatabaseConnection connection = new DatabaseConnection(getConnection());

            // partial database export
            QueryDataSet partialDataSet = new QueryDataSet(connection);
            partialDataSet.addTable("cab", "SELECT * FROM cab");
            FlatXmlDataSet.write(partialDataSet, new FileOutputStream("cabs1.xml"));
        }
}
