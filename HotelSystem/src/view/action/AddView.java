package view.action;

import dao.CustomerDAO;
import dao.RoomDAO;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddView extends JFrame {
    private JTextField nameTF;
    private JComboBox<String> sexBox;
    private JTextField phoneTF;
    private JTextField cardTF;
    private JComboBox<String> addroomBox;
    private JTextField depositTF;

    public AddView() {
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("入住办理");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
    }

    private void initUI() {
        //主界面
        JPanel add = new JPanel(null);
        Font labelFont = new Font("微软雅黑", Font.BOLD, 18);
        add.setBackground(new Color(240, 245, 250));
        add(add);

        // 标题
        JLabel titleLabel = new JLabel("管理员菜单");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(new Color(20, 53, 87));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 1200, 40);
        titleLabel.setBackground(new Color(80, 165, 230));
        titleLabel.setOpaque(true);
        add.add(titleLabel);

        // 返回
        JButton reBT = new JButton("返回");
        reBT.setFont(labelFont);
        reBT.setBackground(new Color(25, 100, 180));
        reBT.setForeground(Color.WHITE);
        reBT.setFocusPainted(false);
        reBT.setBounds(0, 0, 80, 40);
        reBT.addActionListener(this::reAction);
        add.add(reBT);

        // 房客姓名
        JLabel nameLabel = new JLabel("房客姓名:");
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        nameLabel.setBounds(400, 100, 100, 40);
        add.add(nameLabel);

        // 姓名输入框
        nameTF = new JTextField();
        nameTF.setFont(new Font("微软雅黑", Font.BOLD, 22));
        nameTF.setBounds(500, 105, 250, 35);
        add.add(nameTF);

        // 房客性别
        JLabel sexLabel = new JLabel("房客性别:");
        sexLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        sexLabel.setBounds(400, 180, 100, 40);
        add.add(sexLabel);

        // 性别输入框
        String[] sexOptions = {"男", "女"};
        sexBox = new JComboBox<>(sexOptions);
        sexBox.setFont(new Font("微软雅黑", Font.BOLD, 22));
        sexBox.setBounds(500, 185, 250, 35);
        sexBox.setSelectedIndex(-1);
        add.add(sexBox);

        // 房客电话
        JLabel Label = new JLabel("房客电话:");
        Label.setFont(new Font("微软雅黑", Font.BOLD, 22));
        Label.setBounds(400, 260, 100, 40);
        add.add(Label);

        // 电话输入框
        phoneTF = new JTextField();
        phoneTF.setFont(new Font("微软雅黑", Font.BOLD, 22));
        phoneTF.setBounds(500, 265, 250, 35);
        add.add(phoneTF);

        // 房客身份证件
        JLabel cardLabel = new JLabel("身份证号:");
        cardLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        cardLabel.setBounds(400, 340, 100, 40);
        add.add(cardLabel);

        // 身份证输入框
        cardTF = new JTextField();
        cardTF.setFont(new Font("微软雅黑", Font.BOLD, 22));
        cardTF.setBounds(500, 345, 250, 35);
        add.add(cardTF);

        // 空闲房间
        JLabel roomLabel = new JLabel("空闲房间:");
        roomLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        roomLabel.setBounds(400, 420, 100, 40);
        add.add(roomLabel);

        // 空闲房间号下拉框
        addroomBox = new JComboBox<>();
        try {
            RoomDAO dao = new RoomDAO();
            for (String room_num : dao.getroom_num_null()) {
                addroomBox.addItem(room_num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        addroomBox.setBounds(500, 425, 250, 35);
        addroomBox.setFont(labelFont);
        addroomBox.setSelectedIndex(-1);
        add.add(addroomBox);

        // 押金
        JLabel depositLabel = new JLabel("房客押金:");
        depositLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        depositLabel.setBounds(400, 500, 100, 40);
        add.add(depositLabel);

        // 押金输入框
        depositTF = new JTextField();
        depositTF.setFont(new Font("微软雅黑", Font.BOLD, 22));
        depositTF.setBounds(500, 505, 250, 35);
        add.add(depositTF);

        // 添加
        JButton addBT = new JButton("添加信息");
        addBT.setFont(new Font("微软雅黑", Font.BOLD, 18));
        addBT.setBackground(new Color(25, 100, 180));
        addBT.setForeground(Color.WHITE);
        addBT.setFocusPainted(false);
        addBT.setBounds(500, 580, 200, 45);
        addBT.addActionListener(this::addAction);
        add.add(addBT);
    }

    private void reAction(ActionEvent e) {
        this.dispose();
        new MainView().setVisible(true);

    }

    private void addAction(ActionEvent e) {
        String name = nameTF.getText().trim();
        String sex = (String) sexBox.getSelectedItem();
        String phone = phoneTF.getText().trim();
        String card = cardTF.getText().trim();
        String room = (String) addroomBox.getSelectedItem();
        String deposit = depositTF.getText().trim();

        try {
            int customerId = CustomerDAO.addCustomer(name, sex, phone, card, deposit);  // 插入客户信息并获取customer_id

            if (customerId > 0) {
                boolean result = CustomerDAO.addRecord(customerId, room);

                if (result) {
                    JOptionPane.showMessageDialog(this, "入住记录已成功添加！");
                } else {
                    JOptionPane.showMessageDialog(this, "添加失败！", "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "客户信息添加失败！", "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "发生异常：" + ex.getMessage(),
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
