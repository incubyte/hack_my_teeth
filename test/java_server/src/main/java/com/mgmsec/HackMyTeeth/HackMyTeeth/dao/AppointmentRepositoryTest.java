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

        // Process the resultSet and assert the results
    }

    @Test
    public void testAddAppointment() {
        // Create an Appointment object with test data

        // Call the addAppointment method with the test data

        // Assert the result
    }
}