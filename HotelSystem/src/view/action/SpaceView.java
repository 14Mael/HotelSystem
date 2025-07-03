package view.action;

import dao.RoomDAO;
import view.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SpaceView extends JFrame {
    private JTable resultTb;

    public SpaceView() {
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("查询空闲房间");
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

        // 查询
        JButton searchBT = new JButton("查询");
        searchBT.setFont(new Font("微软雅黑", Font.BOLD, 18));
        searchBT.setBackground(new Color(25, 100, 180));
        searchBT.setForeground(Color.WHITE);
        searchBT.setFocusPainted(false);
        searchBT.setBounds(500, 50, 200, 45);
        searchBT.addActionListener(this::searchAction);
        search.add(searchBT);
        // 结果表格
        String[] columnNames = {"房间号", "状态", "类型", "价格"};
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
        try {
            RoomDAO roomDAO = new RoomDAO();
            List<String[]> spareRooms = roomDAO.getSpareRooms();
            displayResults(spareRooms);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "查询失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayResults(List<String[]> data) {
        DefaultTableModel model = (DefaultTableModel) resultTb.getModel();
        model.setRowCount(0);

        for (String[] row : data) {
            model.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
