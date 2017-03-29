package spring_jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;


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
        //dao.insertCab(map);


        // STEP 2: SELECT operator
        log.info("Amount of cabs " + dao.countOfCabsWithCapacityMoreThan(10));


        // STEP 3: DDL operator
        //dao.createUserTable();


        // STEP 4: Map Product object
        dao.getCabs().forEach(log::info);


    }
}
