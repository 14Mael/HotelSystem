package view.action;

import dao.CustomerDAO;
import dao.RecordDAO;
import dao.RoomDAO;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class DelView extends JFrame {
    private JComboBox<String> delnameBox;
    private JComboBox<String> roomNumBox;


    public DelView() {
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("退房办理");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
    }

    private void initUI() {
        // 主界面
        JPanel del = new JPanel(null);
        Font labelFont = new Font("微软雅黑", Font.BOLD, 18);
        del.setBackground(new Color(240, 245, 250));
        add(del);
        // 标题
        JLabel titleLabel = new JLabel("管理员菜单");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(new Color(20, 53, 87));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 1200, 40);
        titleLabel.setBackground(new Color(80, 165, 230));
        titleLabel.setOpaque(true);
        del.add(titleLabel);
        // 返回
        JButton reBT = new JButton("返回");
        reBT.setFont(labelFont);
        reBT.setBackground(new Color(25, 100, 180));
        reBT.setForeground(Color.WHITE);
        reBT.setFocusPainted(false);
        reBT.setBounds(0, 0, 80, 40);
        reBT.addActionListener(this::reAction);
        del.add(reBT);
        // 房客姓名
        JLabel nameLabel = new JLabel("房客姓名:");
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        nameLabel.setBounds(400, 100, 100, 40);
        del.add(nameLabel);
        // 已入住房客
        delnameBox = new JComboBox<>();
        delnameBox.setBounds(500, 105, 250, 35);
        delnameBox.setFont(labelFont);
        delnameBox.setSelectedIndex(-1);
        delnameBox.setEditable(true);
        del.add(delnameBox);
        fillNameComboBox();
        // 入住房间
        JLabel roomLabel = new JLabel("入住房间:");
        roomLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        roomLabel.setBounds(400, 180, 100, 40);
        del.add(roomLabel);
        // 已入住房间号
        roomNumBox = new JComboBox<>();
        roomNumBox.setBounds(500, 185, 250, 35);
        roomNumBox.setFont(labelFont);
        roomNumBox.setSelectedIndex(-1);
        roomNumBox.setEditable(true);
        del.add(roomNumBox);
        fillRoomComboBox();
        // 退房按钮
        JButton checkoutButton = new JButton("退房");
        checkoutButton.setFont(labelFont);
        checkoutButton.setBackground(new Color(25, 100, 180));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.setBounds(500, 260, 200, 40);
        checkoutButton.addActionListener(this::checkoutAction);
        del.add(checkoutButton);

    }

    private void reAction(ActionEvent e) {
        new MainView().setVisible(true);
        this.dispose();
    }

    private void fillNameComboBox() {
        CustomerDAO customerDAO = new CustomerDAO();
        List<String> names = customerDAO.getCheckinName();
        for (String name : names) {
            delnameBox.addItem(name);
        }
    }

    private void fillRoomComboBox() {
        RoomDAO roomDAO = new RoomDAO();
        try {
            List<String> roomNumbers = roomDAO.getroom_num_full();
            for (String roomNumber : roomNumbers) {
                roomNumBox.addItem(roomNumber);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载房间号时发生错误！");
        }
    }

    private void checkoutAction(ActionEvent e) {
        String Name = (String) delnameBox.getSelectedItem();
        String RoomNum = (String) roomNumBox.getSelectedItem();

        if (Name == null || Name.isEmpty() || RoomNum == null || RoomNum.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择有效的房客姓名和房间号！");
            return;
        }

        RecordDAO recordDAO = new RecordDAO();
        try {
            int recordId = recordDAO.getRecordId(Name, RoomNum);
            if (recordId == -1) {
                JOptionPane.showMessageDialog(this, "未找到对应的记录，请检查输入信息是否正确。");
                return;
            }

            //因为这里的存储过程成功时的返回值是0
            boolean success = recordDAO.CheckoutProcedure(recordId);
            // 所以这里取非 表示成功执行
            if (!success) {
                JOptionPane.showMessageDialog(this, "退房成功！");
            } else {
                JOptionPane.showMessageDialog(this, "退房失败，请检查输入信息是否正确。");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "退房时发生错误！\n" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}