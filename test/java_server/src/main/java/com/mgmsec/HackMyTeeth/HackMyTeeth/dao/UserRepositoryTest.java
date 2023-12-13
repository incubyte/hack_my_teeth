import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testFindByUsername() {
        String username = "testUser";
        String password = "testPassword";

        String result = userRepository.findByUsername(username, password);

        // Assert the result
        // ...
    }

    @Test
    public void testGetDentistById() {
        String id = "1";
        boolean sqli = false;

        User result = userRepository.getDentistById(id, sqli);

        // Assert the result
        // ...
    }

    @Test
    public void testGetSalt() {
        String username = "testUser";

        String result = userRepository.getSalt(username);

        // Assert the result
        // ...
    }

    @Test
    public void testFindByUser() {
        String username = "testUser";
        String password = "testPassword";

        List<User> result = userRepository.findByUser(username, password);

        // Assert the result
        // ...
    }

    @Test
    public void testChangePassword() {
        int id = 1;
        String password = "newPassword";

        boolean result = userRepository.changePassword(id, password);

        // Assert the result
        // ...
    }

    @Test
    public void testUpdateAllSaltColumn() {
        List<String> salts = List.of("salt1", "salt2", "salt3");

        userRepository.updateAllSaltColumn(salts);

        // Assert the changes in the database
        // ...
    }

    @Test
    public void testResetAllPassword() {
        List<String> passwords = List.of("password1", "password2", "password3");

        userRepository.resetAllPassword(passwords);

        // Assert the changes in the database
        // ...
    }
}