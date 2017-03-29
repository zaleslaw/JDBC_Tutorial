1.**Spring JDBC. Client Application**

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
        System.out.println("Amount of male customers " + dao.countOfCabsWithCapacityMoreThan(10));


        // STEP 3: DDL operator
        dao.createUserTable();


        // STEP 4: Map Product object
        dao.getCabs().forEach(System.out::println);
        
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
add file _beansForSpringJDBC.xml_

    <bean id="taxiDao" class="spring_jdbc.TaxiDao">
        <property name="dataSource" ref="dataSource"/>
    </bean>
   
Let's use the most simple BasicDataSource
   
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
        <property name="driverClassName" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>


        <property name="removeAbandoned" value="true"/>
        <property name="initialSize" value="10" />
        <property name="maxActive" value="5" />
    </bean>

    <context:property-placeholder location="jdbc.properties"/>
      

