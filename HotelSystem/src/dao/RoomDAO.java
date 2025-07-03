package dao;

import java.sql.*;
import java.util.*;

public class RoomDAO extends BaseDAO {
    public List<String> getroom_num() throws ClassNotFoundException, SQLException {
        List<String> options = new ArrayList<>();
        String sql = "SELECT room_num FROM room";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                options.add(rs.getString("room_num"));
            }
        }
        return options;
    }

    //已入住
    public List<String> getroom_num_full() throws ClassNotFoundException, SQLException {
        List<String> options = new ArrayList<>();
        String sql = "SELECT room_num FROM room WHERE status = '已入住'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                options.add(rs.getString("room_num"));
            }
        }
        return options;
    }

    //空闲
    public List<String> getroom_num_null() throws ClassNotFoundException, SQLException {
        List<String> options = new ArrayList<>();
        String sql = "SELECT room_num FROM room WHERE status = '空闲'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                options.add(rs.getString("room_num"));
            }
        }
        return options;
    }

    public boolean addRoom(String roomNum, int typeId) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO room (room_num, type_id, status) VALUES (?, ?, '空闲')";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomNum);
            ps.setInt(2, typeId);

            int result = ps.executeUpdate();
            return result > 0;
        }
    }

    public List<String[]> getSpareRooms() throws ClassNotFoundException, SQLException {
        List<String[]> spareRooms = new ArrayList<>();
        String sql = "SELECT * FROM spare_rooms";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String roomNum = rs.getString("room_num");
                String status = rs.getString("status");
                String typeName = rs.getString("type_name");
                String price = rs.getString("Price");
                spareRooms.add(new String[]{roomNum, status, typeName, price});
            }
        }
        return spareRooms;
    }
}
