package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomtypeDAO extends BaseDAO{
    // 获取房间类型名
    public List<String> getroom_type() throws ClassNotFoundException, SQLException {
        List<String> options = new ArrayList<>();
        String sql = "SELECT type_name FROM room_type";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                options.add(rs.getString("type_name"));
            }
        }
        return options;
    }

    // 通过房间类型名获取类型id
    public int getTypeId(String typeName) throws ClassNotFoundException, SQLException {
        String sql = "SELECT type_id FROM room_type WHERE type_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typeName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("type_id");
                }
            }
        }
        return -1;
    }
}
