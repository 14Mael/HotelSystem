package dao;

import java.sql.*;

public class UserDAO extends BaseDAO {
    //管理员登录 loginview中的登录按钮时间监控器调用了
    public boolean loginAd(String name, String pwd) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        conn = getConnection();
        String sql1 = "select * from admin where username=? and password=?";

        stmt = conn.prepareStatement(sql1);
        stmt.setString(1, name);
        stmt.setString(2, pwd);

        rs = stmt.executeQuery();
        return rs.next();
    }
}

