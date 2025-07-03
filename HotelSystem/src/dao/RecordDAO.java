package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordDAO extends BaseDAO {
    // 连接record, customer两个表 再通过姓名和房间号来查找
    public int getRecordId(String name, String room_num) throws ClassNotFoundException, SQLException {
        int recordId = -1;
        String sql = "SELECT record_id FROM record, customer " +
                "WHERE (record.customer_id=customer.customer_id) AND (name = ? AND room_num = ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, room_num);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    recordId = rs.getInt("record_id");
                }
            }
        }
        return recordId;
    }

    public boolean processCheckoutWithProcedure(int recordId) throws ClassNotFoundException, SQLException {
        // 检查 recordId 是否有效
        if (!isValidRecordId(recordId)) {
            System.err.println("Invalid recordId: " + recordId);
            throw new SQLException("无效的入住记录");
        }

        String callProcedureSql = "{call sp_checkout(?)}";
        try (Connection conn = getConnection();
             CallableStatement cs = conn.prepareCall(callProcedureSql)) {
            cs.setInt(1, recordId);
            int rowsAffected = cs.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private boolean isValidRecordId(int recordId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT COUNT(*) FROM record WHERE record_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<Map<String, Object>> calculateRoomRevenue(Date startTime, Date endTime) {
        List<Map<String, Object>> records = new ArrayList<>();

        try (Connection conn = getConnection();
             CallableStatement cstmt = conn.prepareCall("{call CalculateRoomRevenue(?, ?)}")) {

            // 设置输入参数
            cstmt.setTimestamp(1, new java.sql.Timestamp(startTime.getTime()));
            cstmt.setTimestamp(2, new java.sql.Timestamp(endTime.getTime()));

            // 执行存储过程
            boolean hasResults = cstmt.execute();

            if (hasResults) {
                try (ResultSet rs = cstmt.getResultSet()) {
                    while (rs.next()) {
                        Map<String, Object> record = new HashMap<>();
                        record.put("类型编号", rs.getString("类型编号"));
                        record.put("房型", rs.getString("房型"));
                        record.put("入住小时数", rs.getBigDecimal("入住小时数"));
                        record.put("费用合计", rs.getBigDecimal("费用合计"));

                        records.add(record);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return records;
    }
}
