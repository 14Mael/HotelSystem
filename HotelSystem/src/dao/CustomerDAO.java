package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CustomerDAO extends BaseDAO {
    // 在customer插入新数据
    public static int addCustomer(String name, String sex, String phone, String card, String deposit)
            throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "INSERT INTO customer(name, sex, phone, id_card,deposit) VALUES (?, ?, ?, ?,?)";
        try {
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, sex);
            ps.setString(3, phone);
            ps.setString(4, card);
            ps.setString(5, deposit);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);  // 返回customer_id
                }
            }
            return -1;  // 插入失败
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
    // 插入room_num和customer_id
    public static boolean addRecord(int customer_id, String room_num)
            throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        String sql = "INSERT INTO record (customer_id, room_num) VALUES (?, ?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, customer_id);
            ps.setString(2, room_num);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    // 获取退房时间为空的人的姓名 退房时间为空代表还没有退房
    public List<String> getCheckinName() {
        List<String> customerNames = new ArrayList<>();
        String sql = "SELECT c.name FROM customer c JOIN Record r " +
                "ON c.customer_id = r.customer_id where checkout_time IS NULL";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customerNames.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerNames;
    }

    public List<String> getName() throws SQLException, ClassNotFoundException {
        List<String> customerNames = new ArrayList<>();
        String sql = "SELECT name FROM customer";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customerNames.add(rs.getString("name"));
            }
        }
        return customerNames;
    }

    // 查询所有顾客
    public List<String[]> getAllCustomer() throws SQLException, ClassNotFoundException {
        List<String[]> customer = new ArrayList<>();
        String sql = "SELECT * FROM customer,record where (record.customer_id=customer.customer_id)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String checkintime = rs.getString("checkin_time");
                String checkouttime = rs.getString("checkout_time");
                String customerName = rs.getString("name");
                String roomNum = rs.getString("room_num");
                String[] customerData = {checkintime,checkouttime, customerName, roomNum};
                customer.add(customerData);
            }
        }
        return customer;
    }

    // 按照名字查询或模糊查询
    public List<String[]> getCustomersByName(String name) throws SQLException, ClassNotFoundException {
        List<String[]> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer,record WHERE (record.customer_id=customer.customer_id) AND name LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String checkintime = rs.getString("checkin_time");
                    String checkouttime = rs.getString("checkout_time");
                    String customerName = rs.getString("name");
                    String roomNum = rs.getString("room_num");
                    String[] customerData = {checkintime,checkouttime, customerName, roomNum};
                    customers.add(customerData);
                }
            }
        }
        return customers;
    }

    // 按照房间号查询
    public List<String[]> getCustomersByRoom(String roomNumber) throws SQLException, ClassNotFoundException {
        List<String[]> customer = new ArrayList<>();
        String sql = "SELECT * FROM customer,record WHERE (record.customer_id=customer.customer_id) AND room_num = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roomNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String checkintime = rs.getString("checkin_time");
                    String checkouttime = rs.getString("checkout_time");
                    String customerName = rs.getString("name");
                    String roomNum = rs.getString("room_num");
                    String[] customerData = {checkintime,checkouttime, customerName, roomNum};
                    customer.add(customerData);
                }
            }
        }
        return customer;
    }

    // 两者都有值，则两者都是条件
    public List<String[]> getCustomerByRoomAndName(String roomNumber, String name)
            throws SQLException, ClassNotFoundException {
        List<String[]> customer = new ArrayList<>();
        String sql = "SELECT * FROM customer,record WHERE (record.customer_id=customer.customer_id) " +
                "AND room_num = ? AND name LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roomNumber);
            stmt.setString(2, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String checkintime = rs.getString("checkin_time");
                    String checkouttime = rs.getString("checkout_time");
                    String customerName = rs.getString("name");
                    String roomNum = rs.getString("room_num");
                    String[] customerData = {checkintime,checkouttime, customerName, roomNum};
                    customer.add(customerData);
                }
            }
        }
        return customer;
    }
}

