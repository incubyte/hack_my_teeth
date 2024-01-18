package com.mgmsec.HackMyTeeth.HackMyTeeth.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTestDatabaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTestDatabaseAutoConfiguration.ReplaceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.mgmsec.HackMyTeeth.HackMyTeeth.model.User;
import com.mgmsec.HackMyTeeth.HackMyTeeth.setting.SecuritySettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testFindAll() {
        // Arrange
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "password", "customer", "salt"));
        expectedUsers.add(new User(2L, "Jane", "Smith", "jane.smith@example.com", "janesmith", "password", "dentist", "salt"));

        when(jdbcTemplate.query("SELECT * from user", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt"))))
                .thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userRepository.findAll();

        // Assert
        assertThat(actualUsers).isEqualTo(expectedUsers);
    }

    @Test
    public void testListDentist() {
        // Arrange
        List<User> expectedDentists = new ArrayList<>();
        expectedDentists.add(new User(2L, "Jane", "Smith", "jane.smith@example.com", "janesmith", "password", "dentist", "salt"));

        when(jdbcTemplate.query("select * from user  where role = 1", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt"))))
                .thenReturn(expectedDentists);

        // Act
        List<User> actualDentists = userRepository.listDentist();

        // Assert
        assertThat(actualDentists).isEqualTo(expectedDentists);
    }

    @Test
    public void testSearchDentistWithKey() {
        // Arrange
        String key = "John";
        List<User> expectedDentists = new ArrayList<>();
        expectedDentists.add(new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "password", "customer", "salt"));

        when(jdbcTemplate.query("select * from user  where role = 1 AND (firstName LIKE '%" + key + "%' OR lastName LIKE '%" + key + "%')", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt"))))
                .thenReturn(expectedDentists);

        // Act
        List<User> actualDentists = userRepository.searchDentist(key);

        // Assert
        assertThat(actualDentists).isEqualTo(expectedDentists);
    }

    @Test
    public void testSearchDentistWithoutKey() {
        // Arrange
        List<User> expectedDentists = new ArrayList<>();
        expectedDentists.add(new User(2L, "Jane", "Smith", "jane.smith@example.com", "janesmith", "password", "dentist", "salt"));

        when(jdbcTemplate.query("select * from user  where role = 1", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt"))))
                .thenReturn(expectedDentists);

        // Act
        List<User> actualDentists = userRepository.searchDentist("");

        // Assert
        assertThat(actualDentists).isEqualTo(expectedDentists);
    }

    @Test
    public void testFindByUsernameAndPassword() {
        // Arrange
        String username = "johndoe";
        String password = "password";
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "password", "customer", "salt"));

        when(jdbcTemplate.query("SELECT * FROM user WHERE username='" + username + "' and password='" + password + "'",
                (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt"))))
                .thenReturn(expectedUsers);

        // Act
        String actualRole = userRepository.findByUsername(username, password);

        // Assert
        assertEquals("customer", actualRole);
    }

    @Test
    public void testGetDentistByIdWithSqli() {
        // Arrange
        String id = "1";
        boolean sqli = true;
        List<User> expectedDentists = new ArrayList<>();
        expectedDentists.add(new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "password", "customer", "salt"));

        when(jdbcTemplate.query("select * from user where userID = ? and role = 1", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")), Integer.valueOf(id)))
                .thenReturn(expectedDentists);

        // Act
        User actualDentist = userRepository.getDentistById(id, sqli);

        // Assert
        assertThat(actualDentist).isEqualTo(expectedDentists.get(0));
    }

    @Test
    public void testGetDentistByIdWithoutSqli() {
        // Arrange
        String id = "1";
        boolean sqli = false;
        List<User> expectedDentists = new ArrayList<>();
        expectedDentists.add(new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "password", "customer", "salt"));

        when(jdbcTemplate.query("select * from user where userID =" + id + " and role = 1", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt"))))
                .thenReturn(expectedDentists);

        // Act
        User actualDentist = userRepository.getDentistById(id, sqli);

        // Assert
        assertThat(actualDentist).isEqualTo(expectedDentists.get(0));
    }

    @Test
    public void testGetSalt() {
        // Arrange
        String username = "johndoe";
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "password", "customer", "salt"));

        when(jdbcTemplate.query("SELECT * FROM user WHERE username=?", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")), username))
                .thenReturn(expectedUsers);

        // Act
        String actualSalt = userRepository.getSalt(username);

        // Assert
        assertEquals("salt", actualSalt);
    }

    @Test
    public void testFindByUserWithSqli() {
        // Arrange
        String username = "johndoe";
        String password = "password";
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "password", "customer", "salt"));

        when(jdbcTemplate.query("SELECT * FROM user WHERE username=? and password=?", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")), username, password))
                .thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userRepository.findByUser(username, password);

        // Assert
        assertThat(actualUsers).isEqualTo(expectedUsers);
    }

    @Test
    public void testFindByUserWithoutSqli() {
        // Arrange
        String username = "johndoe";
        String password = "password";
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "password", "customer", "salt"));

        when(jdbcTemplate.query("SELECT * FROM user WHERE username='" + username + "' and password='" + password + "'", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt"))))
                .thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userRepository.findByUser(username, password);

        // Assert
        assertThat(actualUsers).isEqualTo(expectedUsers);
    }

    @Test
    public void testChangePassword() {
        // Arrange
        int id = 1;
        String password = "newpassword";

        // Act
        boolean result = userRepository.changePassword(id, password);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void testUpdateAllSaltColumn() {
        // Arrange
        List<String> salts = new ArrayList<>();
        salts.add("salt1");
        salts.add("salt2");

        // Act
        userRepository.updateAllSaltColumn(salts);

        // Assert
        // Verify that the update query was executed for each salt
    }

    @Test
    public void testResetAllPassword() {
        // Arrange
        List<String> passwords = new ArrayList<>();
        passwords.add("newpassword1");
        passwords.add("newpassword2");

        // Act
        userRepository.resetAllPassword(passwords);

        // Assert
        // Verify that the update query was executed for each password
    }
}
