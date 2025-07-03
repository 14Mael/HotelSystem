package dao;

import java.sql.*;

public class CostDAO extends BaseDAO {
    // 修改房型的价格
    public static boolean updateCost(String Type, double Price) {
        String sql = "UPDATE room_type SET price = ? WHERE type_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, Price);
            ps.setString(2, Type);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
