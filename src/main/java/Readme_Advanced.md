1.**Spring JDBC. Client Application**

    public class SpringApp {

    public static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("beansForSpringJDBC.xml");

        TaxiDao dao = (TaxiDao) context.getBean("taxiDao");


        Map<String, Object> map = new HashMap<>();
        map.put("manufacture_year", new Date(1000));
        map.put("car_make", "super_moskvich");
        map.put("licence_plate", "ZZZ");
        map.put("capacity", "1");
        map.put("has_baby_chair", true);


        // STEP 1: INSERT operator
        dao.insertCab(map);


        // STEP 2: SELECT operator
        log.info("Amount of cabs " + dao.countOfCabsWithCapacityMoreThan(10));


        // STEP 3: DDL operator
        dao.createUserTable();


        // STEP 4: Map Product object
        dao.getCabs().forEach(log::info);


    }
    }

        
2.**Cab class**

      public class Cab {
  
      private Date manufactureYear;
      private String carMake;
      private String licencePlate;
      private int capacity;
      private boolean hasBabyChair;
      
      + constructor, empty constructor, getters, setters, toString
      
3.**TaxiDao**

    public class TaxiDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setDataSource(DataSource dataSource) {  // <---- Inject DataSource through setter
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertTemplate = new SimpleJdbcInsert(dataSource).withTableName("cab");
    }

    public void insertCab(Map<String, Object> parameters) {
        insertTemplate.execute(parameters);
    }

    public int countOfCabsWithCapacityMoreThan(int capacity) {

        String sql = "SELECT count(*) FROM cab WHERE capacity > :capacity";

        SqlParameterSource namedParameters = new MapSqlParameterSource("capacity", capacity);

        return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }

    public void createUserTable() {

        String DDLOperator = "CREATE TABLE IF NOT EXISTS users (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(30), PRIMARY KEY (id))";
        this.jdbcTemplate.execute(DDLOperator);


    }

    public void dropUserTable() {

        String DDLOperator = "DROP TABLE users";
        this.jdbcTemplate.execute(DDLOperator);

    }

    public List<Cab> getCabs() {

        return jdbcTemplate.query(
                "SELECT * FROM cab",
                new RowMapper<Cab>() {
                    @Override
                    public Cab mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Cab(
                                rs.getDate("manufacture_year"),
                                rs.getString("car_make"),
                                rs.getString("licence_plate"),
                                rs.getInt("capacity"),
                                rs.getBoolean("has_baby_chair")
                        );
                    }
                });
    }

    public List<Cab> getCabsForBabyTransporting() {

        return jdbcTemplate.query(
                "SELECT * FROM cab WHERE has_baby_chair = ?", new Object[]{true},
                new RowMapper<Cab>() {
                    @Override
                    public Cab mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Cab(
                                rs.getDate("manufacture_year"),
                                rs.getString("car_make"),
                                rs.getString("licence_plate"),
                                rs.getInt("capacity"),
                                rs.getBoolean("has_baby_chair")
                        );
                    }
                });
    }
    }

4.**Spring JDBC XML configuration**

Add file _beansForSpringJDBC.xml_

    <bean id="taxiDao" class="spring_jdbc.TaxiDao">
        <property name="dataSource" ref="dataSource"/>
    </bean>
   
Let's use the most simple BasicDataSource
   
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
        <property name="driverClassName" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

Add path to jdbc.properties

    <context:property-placeholder location="jdbc.properties"/>
    
Run, it works!

5.**Spy logging**

To log your JDBC activity add this dependency

        <dependency>
            <groupId>org.lazyluke</groupId>
            <artifactId>log4jdbc-remix</artifactId>
            <version>0.2.7</version>
        </dependency>
       

add code (change URL and load specific driver)

    public class Spy_Query {
    
        public static final String URL = "jdbc:log4jdbc:mysql://localhost:3306/guber";
        public static final String USER_NAME = "root";
        public static final String PASSWORD = "pass";
    
    
        public static void main(String[] args) throws ClassNotFoundException {
    
            Logger log = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            Class.forName("net.sf.log4jdbc.DriverSpy");
    
            // making one pre-compiled query for all iterations
            try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement("SELECT * FROM cab WHERE capacity = ?")) {
    
                for (int i = 0; i < 30; i++) {
                    st.setInt(1, i); // Setting up int parameter in safe way
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        log.info(rs.getString("car_make"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        private static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        }
    }
 
 
add log4j.properties to work with that correctly 

# Root logger option
log4j.rootLogger=INFO, stdout

# Direct log messages to stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

log4j.logger.jdbc.sqlonly=INFO
log4j.logger.jdbc.sqltiming=INFO
log4j.logger.jdbc.audit=ON
log4j.logger.jdbc.resultset=ERROR
log4j.logger.jdbc.connection=ERROR
log4j.logger.jdbc.resultsettable=ON


jdbc.sqlonly Ч Ћогирует только SQL
jdbc.sqltiming Ч Ћогирует SQL и врем€ выполнени€
jdbc.audit Ч Ћогирует все вызовы JDBC API, кроме работы с ResultSet
jdbc.resultset Ч ¬се вызовы к ResultSet протоколируютс€
jdbc.connection Ч Ћогируютс€ открытие и закрытие соединени€, полезно использовать дл€ поиска утечек соединений

6.**DBUnit**

Old but working approach to test database.

Next code help us to generate test datasets

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
    
After that write class extending _DBTestCase_

public class FirstTest extends DBTestCase {



    public FirstTest(){
        super();
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.jdbc.Driver" );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost:3306/guber?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8" );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root" );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "pass" );
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

copy code above and write your own tests

    public void testCheckCabs() throws Exception {
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable factTable = databaseDataSet.getTable("cab");


        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/main/resources/datasets/cabs1.xml"));
        ITable testTable = expectedDataSet.getTable("cab");

        Assertion.assertEquals(testTable, factTable);

    }

      

