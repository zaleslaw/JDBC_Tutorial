1. Go to Guber.sql
2. Import schema to MySQL Workbench
3. **Short path to JDBC intro**

add constans

    public static final String URL = "jdbc:mysql://localhost:3306/guber";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "pass";
    
make connection

        Properties props = new Properties();
        props.setProperty("user", USER_NAME);
        props.setProperty("password", PASSWORD);
        try {
            Connection connection = DriverManager.getConnection(URL, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
add to resources **log4j2.properties**

name=JDBC_Tutorial_Properties_Config
appenders = console

appender.console.type = Console
appender.console.name = STDOUT
appender.console.layout.type = PatternLayout
appender.console.layout.pattern = [%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n

rootLogger.level = debug
rootLogger.appenderRefs = stdout
rootLogger.appenderRef.stdout.ref = STDOUT

and add _Logger log = LogManager.getRootLogger();_ to configure logger instead System.out.println

            log.info("Current catalog is " + connection.getCatalog());

            /*
            TRANSACTION_NONE = 0
            TRANSACTION_READ_UNCOMMITTED = 1
            TRANSACTION_READ_COMMITTED = 2
            TRANSACTION_REPEATABLE_READ = 4
            TRANSACTION_SERIALIZABLE = 8
             */
            log.info("Current transaction isolation code is " + connection.getTransactionIsolation());

This is a base sample which will be developed later

4.**Java 6 vs Java 7**

Add some functionality

 public static void main(String[] args) throws SQLException //Task1: Drop throws section
    {
        Logger log = LogManager.getRootLogger();

        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;

        /*try {*/
        connection = getConnection();
        st = connection.createStatement();

        rs = st.executeQuery("SELECT * FROM driver WHERE lastname LIKE 'De%'");
        while (rs.next()) {
            log.info(rs.getRow() + " " + rs.getString(2) + " " + rs.getString("lastname") + " " + rs.getDate("birthdate").toLocalDate().getYear());
        }
        //Solution: Don't forget required handling of null/SQLExceptions
        /*} catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs!=null){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(st!=null){
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/


    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
    
and transform to Java 7

          try (Connection connection = getConnection(); Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM driver WHERE lastname LIKE 'De%'");
            while (rs.next()) {
                log.info(rs.getRow() + " " + rs.getString(2) + " " + rs.getString("lastname") + " " + rs.getDate("birthdate").toLocalDate().getYear());
          }
          
5.**Prepare your statements**

imagine parametrized query based on report settings
        
         try (Connection connection = getConnection(); Statement st = connection.createStatement()) {
                    for (int i = 0; i < 30; i++) {
                        log.info("Round #" + i);
                        ResultSet rs = st.executeQuery("SELECT * FROM cab WHERE capacity = " + i); // new query for each iteration; i - unsafe paramenter
                        while (rs.next()) {
                            log.info(rs.getString("car_make"));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        
        
and prepare your statements        
        
        try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement("SELECT * FROM cab WHERE capacity = ?")) {

            for (int i = 0; i < 30; i++) {
                log.info("Round #" + i);
                st.setInt(1, i); // Setting up int parameter in safe way
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    log.info(rs.getString("car_make"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
6.**Move constants to property file**
First of all, make **jdbc.properties** in **resource** folder and fill it with

jdbc.driverClassName = com.mysql.jdbc.Driver
jdbc.url = jdbc:mysql://localhost:3306/guber?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8
jdbc.username = root
jdbc.password = pass

After that change **getConnection()** method to 

 private static Connection getConnection() throws SQLException {

        Properties prop = new Properties();

        try (InputStream input = new FileInputStream("src/main/resources/jdbc.properties");) {
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("File not found " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return DriverManager.getConnection(
                prop.getProperty("jdbc.url"),
                prop.getProperty("jdbc.username"),
                prop.getProperty("jdbc.password")
        );
    }
    
Run previos example with new property population

7.**Connectable class**

Move next code to Connectable class to extend it in next samples

    public class Connectable {

    public static final Logger log = LogManager.getRootLogger();

    public static Connection getConnection() throws SQLException {

        Properties prop = new Properties();

        try (InputStream input = new FileInputStream("src/main/resources/jdbc.properties");) {
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("File not found " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return DriverManager.getConnection(
                prop.getProperty("jdbc.url"),
                prop.getProperty("jdbc.username"),
                prop.getProperty("jdbc.password")
        );
    }
    }

8.**Strange cursor**
This example demonstrates strange behavior of TYPE_FORWARD_ONLY cursor with JDBC-MySQL driver
Yes, you can scroll it in forward and backward directions

        public class Ex_1_Cursor_Jumping extends Connectable {

        public static void main(String[] args) throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT * FROM taxi_ride",
                     ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {

            ResultSet rs = st.executeQuery();
            rs.first();
            log.info("First " + rs.getRow() + ". " + rs.getString("start_point")
                    + "\t" + rs.getString("end_point"));
            rs.last();
            log.info("Last " + rs.getRow() + ". " + rs.getString("start_point")
                    + "\t" + rs.getString("end_point"));

            rs.afterLast();

            // Unbelievable, but it works
            log.info("DESC ORDER");
            while (rs.previous()) {
                log.info(rs.getRow() + ". " + rs.getString("start_point")
                        + "\t" + rs.getString("end_point"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        }
        }
        
        
9.**Update row through ResultSet**

Also you are able to update row in the last position using .insertRow()

                 try (Connection connection = getConnection();
                     PreparedStatement st = connection.prepareStatement("SELECT * FROM taxi_company ");
                     ResultSet rs = st.executeQuery()) {
                    log.info("DESC ORDER");
                    rs.afterLast();
                    while (rs.previous()) {
                        int rate = rs.getInt(4);
                        rs.updateInt(4, rate + 1);
                        rs.updateRow();  //<-------------------------------- Special command
                    }
        
                    while (rs.next()) {
                        log.info(rs.getRow() + ". " + rs.getString(2) + "\t" + rs.getString(4));
                    }
                    
 But it doen't work!
 The problem is ~~com.mysql.jdbc.NotUpdatable: Result Set not updatable.This result set must come from a statement that was created with a result set type of ResultSet.CONCUR_UPDATABLE, the query must select only one table, can not use functions and must select all primary keys from that table~~
 
 Let's solve it with one modification 
 
    PreparedStatement st = connection.prepareStatement("SELECT * FROM taxi_company ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    
10.**Insert row**
Also you are able to insert row in the last position using .insertRow()

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT * FROM taxi_company", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = st.executeQuery()) {


            log.info("DESC ORDER");
            rs.last();
            // Begin "INSERT" mode
            rs.moveToInsertRow();
            rs.updateInt(1, 4);
            rs.updateString(2, "NETT");
            rs.updateString(3, "Default City");
            rs.updateInt(4, 8888);
            rs.insertRow();
            // End "INSERT" mode

            rs.beforeFirst();
            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString(2) + "\t" + rs.getString(4));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        
11.**Delete row**
Don't forget delete row which was inserted before

         try (Connection connection = getConnection();
                     PreparedStatement st = connection.prepareStatement("SELECT * FROM taxi_company", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                     ResultSet rs = st.executeQuery()) {
        
                    rs.afterLast();
                    while (rs.previous()) {
                        String name = rs.getString(2);
                        if (name.equals("NETT")) {
                            rs.deleteRow();
                        }
        
                    }
        
                    rs.beforeFirst();
                    while (rs.next()) {
                        log.info(rs.getRow() + ". " + rs.getString(2) + "\t" + rs.getString(4));
                    }
        
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
12.**Magic deletion**

Sometimes some magic stuff can happened
Imagine next situation: SQL and JDBC approaches were mixed in one method

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("DELETE FROM taxi_company WHERE name = 'NETT'");
             PreparedStatement selectSt = connection.prepareStatement("SELECT * FROM taxi_company");
             ResultSet rs = selectSt.executeQuery()
        ) {
            int result = st.executeUpdate();
            log.info(result + " rows were deleted");

            // Deleted row will be printed. What a Terrible Failure?
            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString(2) + "\t" + rs.getString(4));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        
What's the problem? How to fix it?
Maybe change SCROLL SENSETIVITY?
No, it doesn't help!

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("DELETE FROM taxi_company WHERE name = 'NETT'");
             PreparedStatement selectSt = connection.prepareStatement("SELECT * FROM taxi_company"); //ResultSet.TYPE_SCROLL_INSENSITIVE or his friend couldn't help you
             //ResultSet rs = selectSt.executeQuery() // Cursor holds set of rows

        ) {
            int result = st.executeUpdate();
            ResultSet rs = selectSt.executeQuery();
            log.info(result + " rows were deleted");
            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString(2) + "\t" + rs.getString(4));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
 Early created cursor holds set of rows. This is the reason. It can be initialized later, after data removing.
 
         try (Connection connection = getConnection();
              PreparedStatement st = connection.prepareStatement("DELETE FROM taxi_company WHERE name = 'NETT'");
              PreparedStatement selectSt = connection.prepareStatement("SELECT * FROM taxi_company"); //ResultSet.TYPE_SCROLL_INSENSITIVE or his friend couldn't help you
              //ResultSet rs = selectSt.executeQuery() // Cursor holds set of rows
 
         ) {
             int result = st.executeUpdate();
             ResultSet rs = selectSt.executeQuery(); //<============ Move it here
             log.info(result + " rows were deleted");
             while (rs.next()) {
                 log.info(rs.getRow() + ". " + rs.getString(2) + "\t" + rs.getString(4));
             }
 
         } catch (SQLException e) {
             e.printStackTrace();
         }

13.**Change sex**
Let's make complex operation under few persons 
And break in this operation with SQLException 

          try (Connection connection = getConnection();
                      PreparedStatement st = connection.prepareStatement("SELECT * FROM driver WHERE sex = ?");
                      PreparedStatement updateSt = connection.prepareStatement("UPDATE driver SET sex = ? WHERE id = ?")) {
         
                     // SELECT ALL MALES
                     st.setString(1, "M");
                     ResultSet rs = st.executeQuery();
                     log.info("Men List");
                     while (rs.next()) {
                         log.info(rs.getRow() + ". " + rs.getString("firstname") + "\t" + rs.getString("lastname"));
                     }
         
                     // UPDATE SEX FOR ONE MAN AND ONE WOMAN
                     updateSt.setString(1, "F");
                     updateSt.setInt(2, 3);
                     updateSt.executeUpdate();
         
         
                     // This code emulates broken transaction
                     if (new Random().nextBoolean()) { // Sometimes shit happens, if you need it everytime use if(true)
                         throw new SQLException("Database was broken");
                     }
         
                     updateSt.setString(1, MALE);
                     updateSt.setInt(2, 2);
                     updateSt.executeUpdate();
         
                     log.info("Sex was exchanged");
         
                     // SELECT ALL FEMALES
                     st.setString(1, FEMALE);
                     rs = st.executeQuery();
                     log.info("Women List");
                     while (rs.next()) {
                         log.info(rs.getRow() + ". " + rs.getString("firstname") + "\t" + rs.getString("lastname"));
                     }
         
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }

13.**Batches**

Union in one batch all changes

                 // SELECT ALL MALES
                  st.setString(1, MALE);
                  ResultSet rs = st.executeQuery();
                  log.info("Men List");
                  while (rs.next()) {
                      log.info(rs.getRow() + ". " + rs.getString("firstname") + "\t" + rs.getString("lastname"));
                  }
      
                  // UPDATE SEX FOR ONE MAN AND ONE WOMAN
                  updateSt.setString(1, FEMALE);
                  updateSt.setInt(2, 2);
                  updateSt.addBatch();
      
      
                  updateSt.setString(1, MALE);
                  updateSt.setInt(2, 3);
                  updateSt.addBatch();
      
                  final int[] ints = updateSt.executeBatch();
                  log.info("Batch results: " + Arrays.toString(ints));
      
                  log.info("Sex was exchanged");
      
                  // SELECT ALL FEMALES
                  st.setString(1, FEMALE);
                  rs = st.executeQuery();
                  log.info("Women List");
                  while (rs.next()) {
                      log.info(rs.getRow() + ". " + rs.getString("firstname") + "\t" + rs.getString("lastname"));
                  }
                  
14.**Naive Transaction in Java 6**
Change sex together or rollback

        Connection connection = null;
        PreparedStatement st = null;
        PreparedStatement updateSt = null;

        try {

            connection = getConnection();
            st = connection.prepareStatement("SELECT * FROM customers WHERE sex = ?");
            updateSt = connection.prepareStatement("UPDATE customers SET sex = ? WHERE id = ?");

            // SELECT ALL MALES
            st.setString(1, MALE);
            ResultSet rs = st.executeQuery();
            System.out.println("Men List");
            while (rs.next()) {
                System.out.println(rs.getRow() + ". " + rs.getString("firstname")
                        + "\t" + rs.getString("lastname"));
            }

            // UPDATE SEX FOR ONE MAN AND ONE WOMAN
            connection.setAutoCommit(false); //<---------- START TRANSACTION
            updateSt.setString(1, FEMALE);
            updateSt.setInt(2, 3);
            updateSt.executeUpdate();


            // This code emulates broken transaction
            if (new Random().nextBoolean()) { // Sometimes shit happens, if you need it everytime use if(true)
                throw new SQLException("Database was broken");
            }

            updateSt.setString(1, MALE);
            updateSt.setInt(2, 2);
            updateSt.executeUpdate();

            log.info("Sex was exchanged");

            // SELECT ALL FEMALES
            st.setString(1, FEMALE);
            rs = st.executeQuery();
            log.info("Women List");
            while (rs.next()) {
                log.info(rs.getRow() + ". " + rs.getString("firstname") + "\t" + rs.getString("lastname"));
            }
            connection.commit(); //<------------ END TRANSACTION

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
15.**Naive transaction in Java 7**
Add try-with-resources section

        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT * FROM customers WHERE sex = ?");
             PreparedStatement updateSt = connection.prepareStatement("UPDATE customers SET sex = ? WHERE id = ?")) {

Clear catch clause

        } catch (Exception e) {
            e.printStackTrace();
            /* Trying to rollback transaction */
            //connection.rollback()

            /* According to the language spec,
             the connection will be closed before the catch clause is executed */
        }
        
Try to rollback: there is a problem. According to the language spec, the connection will be closed before the catch clause is executed.

Possible solution: 2-level try-with-resources

       try (Connection connection = getConnection()) {
            try (PreparedStatement st = connection.prepareStatement("SELECT * FROM driver WHERE sex = ?");
                 PreparedStatement updateSt = connection.prepareStatement("UPDATE driver SET sex = ? WHERE id = ?")) {
                 
                 
        } catch (SQLException e) {
                   /* Trying to rollback transaction */
                       connection.rollback();
                       throw e;
                   }
               } catch (SQLException e) {
                   System.out.println("Second catch clause");
                   e.printStackTrace();
               }
16.**Incorrect SQL query**
Change one SQL query, make them incorrect.

     try (PreparedStatement st = connection.prepareStatement("SELECT * FROM driverss WHERE sex = ?"); // Incorrect table
     
If table, column or something else is not exist in database, we can get 'Can't call rollback when autocommit=true'

Q: How to fix?
A: change start of autocommiting

              try (Connection connection = getConnection()) {
               connection.setAutoCommit(false); //<---------- START TRANSACTION
               try (PreparedStatement st = connection.prepareStatement("SELECT * FROM driver WHERE sex = ?");
                    PreparedStatement updateSt = connection.prepareStatement("UPDATE driver SET sex = ? WHERE id = ?")) {
                    
17.**Naive savepoint usage**
Let's put one savepoint and rollback to it.

     try (Connection connection = getConnection()) {
            connection.setAutoCommit(false); //<---------- START TRANSACTION
            Savepoint savepoint = null;
            try (PreparedStatement st = connection.prepareStatement("SELECT * FROM driver WHERE sex = ?");
                 PreparedStatement updateSt = connection.prepareStatement("UPDATE driver SET sex = ? WHERE id = ?")) {

add savepoint after female changes, but before SQLException emulation
      
     savepoint = connection.setSavepoint("New_Female_was_born");
     
and change catch clause

     } catch (SQLException e) {
                     e.printStackTrace();
                     if (connection != null) {
                         try {
                             connection.rollback(savepoint);
                         /*
                        But first part of transaction (before savepoint) wasn't applied
                        Solution: call connection.commit()
                        */
                             //connection.commit();
                         } catch (SQLException e1) {
                             e1.printStackTrace();
                         }
     
                     }
                 }
             } catch (SQLException e) {
                 e.printStackTrace();
             }
     
There is a problem: first part of transaction (before savepoint) wasn't applied

Q: How to fix?
A: add **connection.commit()** after **connection.rollback()**


18.**RowSet**

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
    
add listeners

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
            
change something

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

19.**Metadata**

Remind yourself hell of Java 6

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