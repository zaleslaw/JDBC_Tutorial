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
