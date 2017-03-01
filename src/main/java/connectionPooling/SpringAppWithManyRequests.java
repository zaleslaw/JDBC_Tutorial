package connectionPooling;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring_jdbc.TaxiDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Let's run with settings
 *    <property name="removeAbandoned" value="true"/>
 <property name="initialSize" value="10" />
 <property name="maxActive" value="5" />
 */
public class SpringAppWithManyRequests {

    public static final int AMOUNT_OF_THREADS = 10;

    public static void main(String[] args) {
        AbstractApplicationContext context = (AbstractApplicationContext) new ClassPathXmlApplicationContext("beansForSpringJDBC.xml");

        TaxiDao dao = (TaxiDao) context.getBean("taxiDao");


        ExecutorService service = new ScheduledThreadPoolExecutor(AMOUNT_OF_THREADS);
        AtomicInteger counter = new AtomicInteger(0);
        Runnable lambda = () -> {
            try {
                dao.createUserTable();
                dao.dropUserTable();
                counter.getAndIncrement();
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
