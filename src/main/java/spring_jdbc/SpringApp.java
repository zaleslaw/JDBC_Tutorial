package spring_jdbc;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexey_Zinovyev on 17-Oct-16.
 */
public class SpringApp {

    public static final String JAVA_LANG = "Java";

    public static void main(String[] args) {
        AbstractApplicationContext context = (AbstractApplicationContext) new ClassPathXmlApplicationContext("beans.xml");

        ShopDao dao = (ShopDao) context.getBean("shopDao");


        Map<String, Object> map = new HashMap<>();
        map.put("name", "SpringToy");
        map.put("weight", 10);
        map.put("category", "SuperToy");


        // STEP 1: INSERT operator
        dao.insertProduct(map);


        // STEP 2: SELECT operator
        System.out.println("Amount of male customers " + dao.countOfCustomersBySex("male"));


        // STEP 3: DDL operator
        dao.createUserTable();


        // STEP 4: Map Product object
        dao.getProducts().forEach(System.out::println);


    }
}
