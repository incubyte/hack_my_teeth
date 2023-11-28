import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class AppointmentRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testFindAll() throws SQLException {
        String username = "testUser";

        String sql = "SELECT a.appID,a.title,a.time, CONCAT(u.firstName,' ',u.lastName) as cusName,CONCAT(ut.firstName,' ',ut.lastName) as denName,a.description FROM appointment a JOIN user u JOIN user ut ON a.cusID = u.userID AND a.denID = ut.userID AND a.denID= (SELECT userID FROM user WHERE username=?)";

        PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, username);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            // Process the result set
        }

        resultSet.close();
        preparedStatement.close();
    }

    @Test
    public void testAddAppointment() {
        // Test code for addAppointment method
    }
}