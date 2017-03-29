import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


@RunWith(JUnit4.class)
public class CabTest extends DBTestCase {
    private Properties prop;

    public CabTest() throws Exception {
        super();
        prop = new Properties();
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, prop.getProperty("jdbc.driverClassName"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, prop.getProperty("jdbc.url"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, prop.getProperty("jdbc.username"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, prop.getProperty("jdbc.password"));
    }

    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.NONE;
    }

    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.NONE;
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return null;
    }

    protected void setUpDatabaseConfig(DatabaseConfig config) {
        config.setProperty(DatabaseConfig.PROPERTY_BATCH_SIZE, new Integer(50));
        config.setProperty(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, true);
    }


    @Test
    public void checkCabs() throws Exception {
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable factTable = databaseDataSet.getTable("cab");


        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/main/resources/datasets/cabs1.xml"));
        ITable testTable = expectedDataSet.getTable("cab");

        Assertion.assertEquals(testTable, factTable);

    }
}
