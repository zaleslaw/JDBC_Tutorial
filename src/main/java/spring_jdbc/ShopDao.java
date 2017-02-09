package spring_jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexey_Zinovyev on 31-Oct-16.
 */
public class ShopDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setDataSource(DataSource dataSource) {  // <---- Inject DataSource through setter
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertTemplate = new SimpleJdbcInsert(dataSource).withTableName("products");
    }

    public void insertProduct(Map<String, Object> parameters) {
        insertTemplate.execute(parameters);
    }

    public int countOfCustomersBySex(String sex) {

        String sql = "SELECT count(*) FROM customers WHERE sex = :sex";

        SqlParameterSource namedParameters = new MapSqlParameterSource("sex", sex);

        return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }

    public void createUserTable() {

        String DDLOperator = "CREATE TABLE IF NOT EXISTS users (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(30), PRIMARY KEY (id))";
        this.jdbcTemplate.execute(DDLOperator);


    }

    public List<Product> getProducts() {

        return jdbcTemplate.query(
                "SELECT * FROM products WHERE category = ?", new Object[]{"Vegetable"},
                new RowMapper<Product>() {
                    @Override
                    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Product(rs.getString("name"), rs.getInt("weight"),
                                rs.getString("category"));
                    }
                });
    }
}
