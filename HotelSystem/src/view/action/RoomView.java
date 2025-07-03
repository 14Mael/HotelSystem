package view.action;

import dao.RoomDAO;
import dao.RoomtypeDAO;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RoomView extends JFrame {
    private JTextField roomnumTF;
    private JComboBox<String> roomBox;

    public RoomView() {
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("房间类型信息");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
    }

    private void initUI() {
        // 主界面
        JPanel room = new JPanel(null);
        Font labelFont = new Font("微软雅黑", Font.BOLD, 18);
        room.setBackground(new Color(240, 245, 250));
        add(room);

        // 标题
        JLabel titleLabel = new JLabel("管理员菜单");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(new Color(20, 53, 87));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 1200, 40);
        titleLabel.setBackground(new Color(80, 165, 230));
        titleLabel.setOpaque(true);
        room.add(titleLabel);

        // 返回
        JButton reBT = new JButton("返回");
        reBT.setFont(labelFont);
        reBT.setBackground(new Color(25, 100, 180));
        reBT.setForeground(Color.WHITE);
        reBT.setFocusPainted(false);
        reBT.setBounds(0, 0, 80, 40);
        reBT.addActionListener(this::reAction);
        room.add(reBT);

        // 房间号
        JLabel nameLabel = new JLabel("房间号码:");
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        nameLabel.setBounds(400, 100, 100, 40);
        room.add(nameLabel);

        //房间号下拉框
        roomnumTF = new JTextField();
        roomnumTF.setFont(new Font("微软雅黑", Font.BOLD, 22));
        roomnumTF.setBounds(500, 105, 250, 35);
        room.add(roomnumTF);

        // 空闲房间
        JLabel roomLabel = new JLabel("房间类型:");
        roomLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        roomLabel.setBounds(400, 180, 100, 40);
        room.add(roomLabel);

        // 房间类型下拉框
        roomBox = new JComboBox<>();
        try {
            RoomtypeDAO dao = new RoomtypeDAO();
            for (String roomlist : dao.getroom_type()) {
                roomBox.addItem(roomlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        roomBox.setBounds(500, 185, 250, 35);
        roomBox.setFont(labelFont);
        roomBox.setSelectedIndex(-1);
        room.add(roomBox);

        // 添加
        JButton addBT = new JButton("添加信息");
        addBT.setFont(new Font("微软雅黑", Font.BOLD, 18));
        addBT.setBackground(new Color(25, 100, 180));
        addBT.setForeground(Color.WHITE);
        addBT.setFocusPainted(false);
        addBT.setBounds(500, 260, 200, 45);
        addBT.addActionListener(this::addroomAction);
        room.add(addBT);
    }
    // 返回
    private void reAction(ActionEvent e) {
        this.dispose();
        new MainView().setVisible(true);

    }

    private void addroomAction(ActionEvent actionEvent) {
        String roomNum = roomnumTF.getText();
        String roomType = (String) roomBox.getSelectedItem();

        try {
            RoomDAO roomDAO = new RoomDAO();
            RoomtypeDAO roomtypeDAO = new RoomtypeDAO();
            // 检查房间号是否已存在
            if (roomDAO.getroom_num().contains(roomNum)) {
                JOptionPane.showMessageDialog(this, "房间号已存在，添加失败！",
                        "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                // 通过房间类型名称获取对应的类型ID
                int typeId = roomtypeDAO.getTypeId(roomType);
                if (typeId == -1) {
                    JOptionPane.showMessageDialog(this, "未找到对应的房间类型！",
                            "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 将新房间信息添加到数据库
                boolean isAdded = roomDAO.addRoom(roomNum, typeId);
                if (isAdded) {
                    JOptionPane.showMessageDialog(this, "房间添加成功！",
                            "信息", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "房间添加失败！",
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库操作异常，请重试！",
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
