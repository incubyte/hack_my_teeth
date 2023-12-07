import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SpringBootTest
@ActiveProfiles("test")
public class AppointmentRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testFindAll() {
        String username = "testUser";

        // Create a prepared statement to prevent SQL injection
        String sql = "SELECT a.appID,a.title,a.time, CONCAT(u.firstName,' ',u.lastName) as cusName,CONCAT(ut.firstName,' ',ut.lastName) as denName,a.description FROM appointment a JOIN user u JOIN user ut ON a.cusID = u.userID AND a.denID = ut.userID AND a.denID= (SELECT userID FROM user WHERE username=?)";
        jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) -> {
            // Process the result set
            // ...
            return null;
        });
    }

    @Test
    public void testAddAppointment() throws SQLException {
        Appointment appointment = new Appointment();
        // Set appointment properties

        // Create a prepared statement to prevent SQL injection
        String sql = "INSERT INTO appointment values(?,?,?,?,?,?)";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set statement parameters
            statement.setNull(1, java.sql.Types.NULL);
            statement.setString(2, appointment.getTitle());
            statement.setString(3, appointment.getTime());
            statement.setString(4, appointment.getDescription());
            statement.setInt(5, appointment.getCusID());
            statement.setInt(6, appointment.getDenID());

            // Execute the statement
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}