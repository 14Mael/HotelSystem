package view.action;

import dao.CustomerDAO;
import dao.RoomDAO;
import view.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SearchView extends JFrame {
    private JComboBox<String> roomBox;
    private JComboBox<String> nameBox;
    private JTable resultTb;

    public SearchView() {
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("查询顾客信息");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
    }

    private void initUI() {
        //主界面
        JPanel search = new JPanel(null);
        Font labelFont = new Font("微软雅黑", Font.BOLD, 18);
        search.setBackground(new Color(240, 245, 250));
        add(search);
        // 标题
        JLabel titleLabel = new JLabel("管理员菜单");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(new Color(20, 53, 87));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 1200, 40);
        titleLabel.setBackground(new Color(80, 165, 230));
        titleLabel.setOpaque(true);
        search.add(titleLabel);
        // 返回
        JButton reBT = new JButton("返回");
        reBT.setFont(labelFont);
        reBT.setBackground(new Color(25, 100, 180));
        reBT.setForeground(Color.WHITE);
        reBT.setFocusPainted(false);
        reBT.setBounds(0, 0, 80, 40);
        reBT.addActionListener(this::reAction);
        search.add(reBT);
        // 房间
        JLabel roomLabel = new JLabel("查询房间:");
        roomLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        roomLabel.setBounds(50, 50, 100, 40);
        search.add(roomLabel);

        // 房间号下拉框
        roomBox = new JComboBox<>();
        try {
            RoomDAO dao = new RoomDAO();
            for (String room_num : dao.getroom_num()) {
                roomBox.addItem(room_num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        roomBox.setBounds(150, 55, 150, 35);
        roomBox.setFont(labelFont);
        roomBox.setSelectedIndex(-1);
        roomBox.setEditable(true);
        search.add(roomBox);
        // 姓名
        JLabel nameLabel = new JLabel("顾客姓名:");
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        nameLabel.setBounds(350, 50, 100, 40);
        search.add(nameLabel);
        // 姓名下拉框
        nameBox = new JComboBox<>();
        try {
            CustomerDAO dao = new CustomerDAO();
            for (String name : dao.getName()) {
                nameBox.addItem(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        nameBox.setBounds(450, 55, 150, 35);
        nameBox.setFont(labelFont);
        nameBox.setSelectedIndex(-1);
        nameBox.setEditable(true);
        search.add(nameBox);
        // 查询
        JButton searchBT = new JButton("查询");
        searchBT.setFont(new Font("微软雅黑", Font.BOLD, 18));
        searchBT.setBackground(new Color(25, 100, 180));
        searchBT.setForeground(Color.WHITE);
        searchBT.setFocusPainted(false);
        searchBT.setBounds(775, 50, 200, 45);
        searchBT.addActionListener(this::searchAction);
        search.add(searchBT);
        // 结果表格
        String[] columnNames = {"入住时间", "退房时间", "姓名", "房间号"};
        resultTb = new JTable(new DefaultTableModel(columnNames, 0));
        resultTb.setFont(new Font("微软雅黑", Font.BOLD, 18));
        resultTb.setIntercellSpacing(new Dimension(20, 0));
        resultTb.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(resultTb);
        JTableHeader header = resultTb.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 18));
        scrollPane.setBounds(50, 125, 1100, 525);
        search.add(scrollPane);
    }

    private void reAction(ActionEvent e) {
        new MainView().setVisible(true);
        this.dispose();
    }

    private void searchAction(ActionEvent actionEvent) {
        String room = (String) roomBox.getSelectedItem();
        String name = (String) nameBox.getSelectedItem();

        try {
            CustomerDAO dao = new CustomerDAO();

            if (room == null && name == null) {
                List<String[]> customers = dao.getAllCustomer();
                displayResults(customers);
            } else if (room == null) {
                List<String[]> customers = dao.getCustomersByName(name);
                displayResults(customers);
            } else if (name == null) {
                List<String[]> customers = dao.getCustomersByRoom(room);
                displayResults(customers);
            } else {
                List<String[]> customers = dao.getCustomerByRoomAndName(room, name);
                displayResults(customers);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "查询失败: " +
                    e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayResults(List<String[]> customers) {
        DefaultTableModel model = (DefaultTableModel) resultTb.getModel();
        model.setRowCount(0);

        for (String[] customer : customers) {
            model.addRow(customer);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
