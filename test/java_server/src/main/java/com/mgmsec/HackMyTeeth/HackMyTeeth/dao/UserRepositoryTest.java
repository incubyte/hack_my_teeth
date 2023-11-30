import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testFindByUsernameAndPwd() throws SQLException {
        String username = "testuser";
        String password = "testpassword";

        Connection connection = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE username=? and password=?");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        // Assert the result
        // ...
    }
}