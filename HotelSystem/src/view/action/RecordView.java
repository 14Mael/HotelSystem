package view.action;

import dao.RecordDAO;
import view.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class RecordView extends JFrame {
    private DefaultTableModel tableModel;
    private JSpinner startTimePicker;
    private JSpinner endTimePicker;
    private JTable table;

    public RecordView() {
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("费用信息管理");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
    }

    private void initUI() {
        // 主界面
        JPanel record = new JPanel(null);
        Font labelFont = new Font("微软雅黑", Font.BOLD, 18);
        record.setBackground(new Color(240, 245, 250));
        add(record);

        // 标题
        JLabel titleLabel = new JLabel("管理员菜单");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(new Color(20, 53, 87));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 1200, 40);
        titleLabel.setBackground(new Color(80, 165, 230));
        titleLabel.setOpaque(true);
        record.add(titleLabel);

        // 返回
        JButton reBT = new JButton("返回");
        reBT.setFont(labelFont);
        reBT.setBackground(new Color(25, 100, 180));
        reBT.setForeground(Color.WHITE);
        reBT.setFocusPainted(false);
        reBT.setBounds(0, 0, 80, 40);
        reBT.addActionListener(this::reAction);
        record.add(reBT);

        // 开始时间
        JLabel startLabel = new JLabel("开始时间:");
        startLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        startLabel.setBounds(50, 50, 100, 40);
        record.add(startLabel);

        // 添加开始时间选择器
        startTimePicker = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(startTimePicker, "yyyy-MM-dd HH:mm:ss");
        startTimePicker.setEditor(timeEditor);
        startTimePicker.setBounds(150, 50, 150, 40);
        record.add(startTimePicker);

        // 结束时间
        JLabel endLabel = new JLabel("结束时间:");
        endLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        endLabel.setBounds(350, 50, 100, 40);
        record.add(endLabel);

        // 添加结束时间选择器
        endTimePicker = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimePicker, "yyyy-MM-dd HH:mm:ss");
        endTimePicker.setEditor(endTimeEditor);
        endTimePicker.setBounds(450, 50, 150, 40);
        record.add(endTimePicker);

        // 查询
        JButton searchBT = new JButton("查询");
        searchBT.setFont(new Font("微软雅黑", Font.BOLD, 18));
        searchBT.setBackground(new Color(25, 100, 180));
        searchBT.setForeground(Color.WHITE);
        searchBT.setFocusPainted(false);
        searchBT.setBounds(775, 50, 200, 45);
        searchBT.addActionListener(this::recordAction);
        record.add(searchBT);

        // 结果表格
        String[] columnNames = {"类型编号", "房型", "入住小时数", "费用合计"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                }
                return c;
            }
        };

        table.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        table.setRowHeight(30); // 设置行高
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 18));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1100, 525));
        scrollPane.setBounds(50, 125, 1100, 525);
        record.add(scrollPane);
    }

    private void reAction(ActionEvent e) {
        new MainView().setVisible(true);
        this.dispose();
    }

    private void recordAction(ActionEvent actionEvent) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //
            java.util.Date utilStartTime = (java.util.Date) startTimePicker.getValue();
            java.util.Date utilEndTime = (java.util.Date) endTimePicker.getValue();
            // 将util类型的日期转成sql型日期
            java.sql.Date sqlStartTime = new java.sql.Date(utilStartTime.getTime());
            java.sql.Date sqlEndTime = new java.sql.Date(utilEndTime.getTime());

            RecordDAO recordDAO = new RecordDAO();
            List<Map<String, Object>> records = recordDAO.calculateProcedure(sqlStartTime, sqlEndTime);
            tableModel.setRowCount(0);
            for (Map<String, Object> record : records) {
                Object[] rowData = {
                        record.get("类型编号"),
                        record.get("房型"),
                        record.get("入住小时数"),
                        record.get("费用合计")
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage(),
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