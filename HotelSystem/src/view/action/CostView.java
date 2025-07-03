package view.action;

import dao.CostDAO;
import dao.RoomtypeDAO;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CostView extends JFrame {
    private JComboBox<String> costroomBox;
    private JTextField priceTF;

    public CostView() {
        initFrame();
        initUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }

    private void initFrame() {
        setTitle("房间价格管理");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
    }

    private void initUI() {
        //主界面
        JPanel cost = new JPanel(null);
        Font labelFont = new Font("微软雅黑", Font.BOLD, 18);
        cost.setBackground(new Color(240, 245, 250));
        add(cost);

        // 标题
        JLabel titleLabel = new JLabel("管理员菜单");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(new Color(20, 53, 87));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 1200, 40);
        titleLabel.setBackground(new Color(80, 165, 230));
        titleLabel.setOpaque(true);
        cost.add(titleLabel);

        // 返回
        JButton reBT = new JButton("返回");
        reBT.setFont(labelFont);
        reBT.setBackground(new Color(25, 100, 180));
        reBT.setForeground(Color.WHITE);
        reBT.setFocusPainted(false);
        reBT.setBounds(0, 0, 80, 40);
        reBT.addActionListener(this::reAction);
        cost.add(reBT);

        // 空闲房间
        JLabel roomLabel = new JLabel("房间类型:");
        roomLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        roomLabel.setBounds(400, 100, 100, 40);
        cost.add(roomLabel);

        // 房间类型下拉框
        costroomBox = new JComboBox<>();
        try {
            RoomtypeDAO dao = new RoomtypeDAO();
            for (String room : dao.getroom_type()) {
                costroomBox.addItem(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        costroomBox.setBounds(500, 105, 250, 35);
        costroomBox.setFont(labelFont);
        costroomBox.setSelectedIndex(-1);
        cost.add(costroomBox);

        // 价格
        JLabel nameLabel = new JLabel("修改价格:");
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        nameLabel.setBounds(400, 180, 100, 40);
        cost.add(nameLabel);

        // 价格输入框
        priceTF = new JTextField();
        priceTF.setFont(new Font("微软雅黑", Font.BOLD, 22));
        priceTF.setBounds(500, 185, 250, 35);
        cost.add(priceTF);

        // 修改按钮
        JButton costBT = new JButton("修改");
        costBT.setFont(labelFont);
        costBT.setBackground(new Color(25, 100, 180));
        costBT.setForeground(Color.WHITE);
        costBT.setFocusPainted(false);
        costBT.setBounds(500, 260, 200, 40);
        costBT.addActionListener(this::costAction);
        cost.add(costBT);

    }

    private void reAction(ActionEvent e) {
        this.dispose();
        new MainView().setVisible(true);
    }

    private void costAction(ActionEvent actionEvent) {
        String RoomType = (String) costroomBox.getSelectedItem();
        String Price = priceTF.getText();
        if (RoomType == null || Price.isEmpty()) {
            JOptionPane.showMessageDialog(null, "请选择房型并输入新价格！");
            return;
        }

        try {
            double newPrice = Double.parseDouble(Price);

            boolean isUpdated = CostDAO.updateCost(RoomType, newPrice);
            if (isUpdated) {
                JOptionPane.showMessageDialog(null, "价格更新成功！");
            } else {
                JOptionPane.showMessageDialog(null, "价格更新失败！");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "请输入有效的价格！");
        }
    }
}
