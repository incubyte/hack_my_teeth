```java
public List<User> searchDentist(String key) {
    try {
        if (!key.equals("")) {
            String sql = "select * from user where role = 1 AND (firstName LIKE ? OR lastName LIKE ?)";
            String searchKey = "%" + key + "%";
            List<User> result = jdbcTemplate.query(sql, (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")), searchKey, searchKey);
            return result;
        } else {
            String sql = "select * from user where role = 1";
            List<User> result = jdbcTemplate.query(sql, (rs, rowNum) -> new User(rs.getLong("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("salt")));
            return result;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
```