import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testGetDentistById() {
        String id = "1";
        Boolean sqli = false;

        String sql = "select * from user where userID = ? and role = 1";
        try {
            PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setBoolean(2, sqli);

            // Execute the prepared statement and assert the result
            // ...
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}