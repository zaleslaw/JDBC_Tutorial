package connectionPooling;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Let's run with settings
 *    <property name="removeAbandoned" value="true"/>
 <property name="initialSize" value="10" />
 <property name="maxActive" value="5" />
 */
public class SpringAppWithManyRequests_C3PO {

    public static final int AMOUNT_OF_THREADS = 100;

    public static void main(String[] args) {
        AbstractApplicationContext context = (AbstractApplicationContext) new ClassPathXmlApplicationContext("beansForSpringJDBC_C3PO.xml");




        ExecutorService service = new ScheduledThreadPoolExecutor(AMOUNT_OF_THREADS);
        AtomicInteger counter = new AtomicInteger(0);
        Runnable lambda = () -> {

            int i = 0;
            try {
                ComboPooledDataSource ds = (ComboPooledDataSource) context.getBean("dataSource");
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
                System.out.println(counter.get() + " " + i );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Counter is " + counter.get());
            }
        };


        ((ScheduledThreadPoolExecutor)service).scheduleAtFixedRate(lambda, 0, 100, TimeUnit.MILLISECONDS);

        while(counter.get() < 400){

        }
        try {
            service.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();

    }
}
