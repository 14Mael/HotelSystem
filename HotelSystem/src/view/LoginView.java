package view;

import dao.UserDAO;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class LoginView extends JFrame {
    private final UserDAO userDAO = new UserDAO();
    private JTextField nameTF;
    private JPasswordField pwdTF;

    public LoginView() {
        initFrame();
        initUI();
    }

    private void initUI() {
        // 主面板
        JPanel main = new JPanel(null);
        main.setBackground(new Color(240, 245, 250));
        add(main);
        // 标题
        Font titleFont = new Font("微软雅黑", Font.BOLD, 36);
        JLabel titleLabel = new JLabel("宾馆房客管理系统");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(25, 100, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(450, 100, 300, 50);
        main.add(titleLabel);
        // 用户名
        Font labelFont = new Font("微软雅黑", Font.BOLD, 22);
        JLabel nameLabel = new JLabel("用户账号:");
        nameLabel.setFont(labelFont);
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        nameLabel.setBounds(400, 200, 150, 30);
        main.add(nameLabel);
        // 用户名输入框
        nameTF = new JTextField();
        nameTF.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        nameTF.setBounds(560, 200, 185, 35);
        main.add(nameTF);
        // 密码
        JLabel pwdLabel = new JLabel("用户密码:");
        pwdLabel.setFont(labelFont);
        pwdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        pwdLabel.setBounds(400, 260, 150, 30);
        main.add(pwdLabel);
        // 密码输入框
        pwdTF = new JPasswordField();
        pwdTF.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        pwdTF.setBounds(560, 260, 185, 35);
        main.add(pwdTF);

        // 登录
        JButton submitBT = new JButton("登 录");
        submitBT.setFont(new Font("微软雅黑", Font.BOLD, 18));
        submitBT.setBackground(new Color(25, 100, 180));
        submitBT.setForeground(Color.WHITE);
        submitBT.setFocusPainted(false);
        submitBT.setBounds(500, 330, 200, 45);
        submitBT.addActionListener(this::loginActionPerformed);
        main.add(submitBT);

    }

    private void loginActionPerformed(ActionEvent e) {
        String username = nameTF.getText().trim();
        String password = new String(pwdTF.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空",
                    "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (userDAO.loginAd(username, password)) {
                JOptionPane.showMessageDialog(this, "登录成功！欢迎管理员 " + username,
                        "登录成功", JOptionPane.INFORMATION_MESSAGE);
                new MainView().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "用户名或密码错误",
                        "登录失败", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "数据库连接失败: " + ex.getMessage(),
                    "系统错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void initFrame() {
        setTitle("管理员登录系统");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }
}