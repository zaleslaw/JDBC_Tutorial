package spring_jdbc;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexey_Zinovyev on 17-Oct-16.
 */
public class SpringApp {

    public static final String JAVA_LANG = "Java";

    public static void main(String[] args) {
        AbstractApplicationContext context = (AbstractApplicationContext) new ClassPathXmlApplicationContext("beansForSpringJDBC.xml");

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


    }
}
