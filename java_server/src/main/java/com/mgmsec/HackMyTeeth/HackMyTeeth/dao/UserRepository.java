```java
package com.mgmsec.HackMyTeeth.HackMyTeeth.dao;

import com.mgmsec.HackMyTeeth.HackMyTeeth.model.User;
import com.mgmsec.HackMyTeeth.HackMyTeeth.setting.SecurityEnum.PwdStorage;
import com.mgmsec.HackMyTeeth.HackMyTeeth.setting.SecuritySettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private EntityManager manager;

    @Autowired
    SecuritySettings secSetting;

    public List<User> findAll() {
        try {
            List<User> result = jdbcTemplate.query("SELECT * from user", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"),rs.getString("salt")));

            return result;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    public List<User> listDentist() {
        try {
            List<User> result = jdbcTemplate.query("select * from user  where role = 1", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"),rs.getString("salt")));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> searchDentist(String key) {
        try {
            if (!key.equals("")) {
                List<User> result = jdbcTemplate.query("select * from user  where role = 1 AND (firstName LIKE ? OR lastName LIKE ?)", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"),rs.getString("salt")), "%" + key + "%", "%" + key + "%");
                return result;
            } else {
                List<User> result = jdbcTemplate.query("select * from user  where role = 1", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"),rs.getString("salt")));
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findByUsername(String username, String password) {
        List<User> result = jdbcTemplate.query("SELECT * FROM user WHERE username=? and password=?", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"),rs.getString("salt")), username, password);
        String s1 = "0";
        String s2 = "1";
        if (result.isEmpty()) {
            return "invalid";
        } else if (result.get(0).getRole().equals(s2)) {
            return "dentist";
        } else if (result.get(0).getRole().equals(s1)) {
            return "customer";
        }
        return null;
    }

    public User getDentistById(String id, Boolean sqli) {
        List<User> result;
        if (sqli) {
            int param;
            try {
                param = Integer.valueOf(id);
            } catch (Exception e) {
                e.printStackTrace();
                param = 0;
            }
            String asql = "select * from user where userID = ? and role = 1";
            result = jdbcTemplate.query(asql, (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")), param);

        } else {
            String sql = "select * from user where userID = ? and role = 1";
            result = jdbcTemplate.query(sql, (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"),rs.getString("salt")), id);
        }
        if (result.size() <= 0)
            return null;
        return result.get(0);
    }

    public String getSalt(String username) {
        List<User> result = new ArrayList<User>();

        try {
            result = jdbcTemplate.query("SELECT * FROM user WHERE username=?", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")), username);
            if (result.size() == 0)
                return null;
            else {
                return result.get(0).getSalt();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> findByUser(String username, String password) {
        try {
            List<User> result = new ArrayList<User>();

            if (secSetting.getSqli()) {
                result = jdbcTemplate.query("SELECT * FROM user WHERE username=? and password=?", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")), username, password);
            } else {
                result = jdbcTemplate.query("SELECT * FROM user WHERE username=? and password=?", (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")), username, password);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean changePassword(int id, String password) {
        try {
            jdbcTemplate.update("UPDATE user SET password = ? WHERE userID = ?", password, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    public void updateAllSaltColumn(List<String> salts) {
        try {
            int id = 1;
            for (String salt : salts) {
                jdbcTemplate.update("UPDATE user SET salt = ? WHERE userID = ?", salt, id++);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetAllPassword(List<String> passwords) {
        try {
            int id = 1;
            for (String password : passwords) {
                jdbcTemplate.update("UPDATE user SET password = ? WHERE userID = ?", password, id++);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```