package connectionPooling.demo;


import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Let's run with settings
 * <property name="removeAbandoned" value="true"/>
 * <property name="initialSize" value="10" />
 * <property name="maxActive" value="5" />
 */
public class SpringAppWithManyRequests_DBCP_1 {


    public static void main(String[] args) throws InterruptedException {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("beansForSpringJDBC.xml");

        AtomicInteger counter = new AtomicInteger(0);
        Runnable lambda = () -> {

            int i = 0;
            try {
                BasicDataSource ds = (BasicDataSource) context.getBean("dataSource");
                ds.setMaxActive(11);
                try (Connection connection = ds.getConnection(); Statement st = connection.createStatement()) {
                    ResultSet rs = st.executeQuery("SELECT * FROM driver WHERE lastname LIKE 'De%'");
                    while (rs.next()) {
                        i = rs.getRow();
                    }

                    Thread.sleep(ThreadLocalRandom.current().nextInt(5000));


                } catch (SQLException e) {
                    e.printStackTrace();
                }
                counter.getAndIncrement();
                System.out.println(counter.get() + " " + i);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Counter is " + counter.get());
            }
        };


        while (counter.get() < 400) {
            new Thread(lambda).start();
            Thread.sleep(10);
        }
    }
}
