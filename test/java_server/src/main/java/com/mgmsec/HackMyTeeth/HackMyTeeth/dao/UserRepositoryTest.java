```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserSearchTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testSearchDentist() {
        String key = "John";
        List<User> result = searchDentist(key);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    private List<User> searchDentist(String key) {
        try {
            if (!key.equals("")) {
                String sql = "select * from user where role = 1 AND (firstName LIKE ? OR lastName LIKE ?)";
                String searchKey = "%" + key + "%";
                return jdbcTemplate.query(sql, (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")), searchKey, searchKey);
            } else {
                String sql = "select * from user where role = 1";
                return jdbcTemplate.query(sql, (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
```