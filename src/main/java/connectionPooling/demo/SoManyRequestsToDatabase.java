package connectionPooling.demo;

import jdbc.Chapter_3_How_to_retrieve_data_with_cursors.Connectable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * We send request each 100 milliseconds.
 * Database connection limit is 151 (in my instance of MySQL)
 * After 151-th request we can see com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: Data source rejected establishment of connection,  message from server: "Too many connections"
 */
public class SoManyRequestsToDatabase extends Connectable {
    public static void main(String[] args) {
        ExecutorService service = new ScheduledThreadPoolExecutor(2);
        AtomicInteger counter = new AtomicInteger(0);
        Runnable lambda = () -> {
            try {
                Connection conn = getConnection();
                System.out.println(counter.getAndIncrement());
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Counter is " + counter.get());
            }
        };

        ((ScheduledThreadPoolExecutor)service).scheduleAtFixedRate(lambda, 0, 100, TimeUnit.MILLISECONDS);
        while(counter.get() < 400){
            // Waiting in main thread
        }
        try {
            service.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }
}
