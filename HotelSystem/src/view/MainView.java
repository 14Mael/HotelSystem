package view;

import view.action.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainView extends JFrame {

    public MainView() {
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("管理员系统");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
    }

    private void initUI() {
        // 主界面
        JPanel main = new JPanel(null);
        Font labelFont = new Font("微软雅黑", Font.BOLD, 18);
        main.setBackground(new Color(240, 245, 250));
        add(main);
        // 标题
        JLabel titleLabel = new JLabel("宾馆管理系统");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(new Color(20, 53, 87));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 1200, 40);
        titleLabel.setBackground(new Color(80, 165, 230));
        titleLabel.setOpaque(true);
        main.add(titleLabel);
        // 增加
        JButton addBT = new JButton("入住办理");
        addBT.setFont(labelFont);
        addBT.setBackground(new Color(25, 100, 180));
        addBT.setForeground(Color.WHITE);
        addBT.setFocusPainted(false);
        addBT.setBounds(280, 100, 240, 40);
        addBT.addActionListener(this::addAction);
        main.add(addBT);
        // 删除
        JButton delBT = new JButton("退房办理");
        delBT.setFont(labelFont);
        delBT.setBackground(new Color(25, 100, 180));
        delBT.setForeground(Color.WHITE);
        delBT.setFocusPainted(false);
        delBT.setBounds(280, 200, 240, 40);
        delBT.addActionListener(this::delAction);
        main.add(delBT);
        // 查询
        JButton searchBT = new JButton("查询顾客信息");
        searchBT.setFont(labelFont);
        searchBT.setBackground(new Color(25, 100, 180));
        searchBT.setForeground(Color.WHITE);
        searchBT.setFocusPainted(false);
        searchBT.setBounds(280, 300, 240, 40);
        searchBT.addActionListener(this::searchAction);
        main.add(searchBT);
        // 房间
        JButton roomBT = new JButton("房间类型信息");
        roomBT.setFont(labelFont);
        roomBT.setBackground(new Color(25, 100, 180));
        roomBT.setForeground(Color.WHITE);
        roomBT.setFocusPainted(false);
        roomBT.setBounds(680, 100, 240, 40);
        roomBT.addActionListener(this::roomAction);
        main.add(roomBT);
        // 价格
        JButton costBT = new JButton("房间价格管理");
        costBT.setFont(labelFont);
        costBT.setBackground(new Color(25, 100, 180));
        costBT.setForeground(Color.WHITE);
        costBT.setFocusPainted(false);
        costBT.setBounds(680, 200, 240, 40);
        costBT.addActionListener(this::costAction);
        main.add(costBT);
        // 费用
        JButton updateBT = new JButton("费用信息管理");
        updateBT.setFont(labelFont);
        updateBT.setBackground(new Color(25, 100, 180));
        updateBT.setForeground(Color.WHITE);
        updateBT.setFocusPainted(false);
        updateBT.setBounds(680, 300, 240, 40);
        updateBT.addActionListener(this::updateAction);
        main.add(updateBT);
        // 查询空闲房间
        JButton spaceBT = new JButton("查询空闲房间");
        spaceBT.setFont(labelFont);
        spaceBT.setBackground(new Color(25, 100, 180));
        spaceBT.setForeground(Color.WHITE);
        spaceBT.setFocusPainted(false);
        spaceBT.setBounds(280, 400, 240, 40);
        spaceBT.addActionListener(this::spaceAction);
        main.add(spaceBT);

    }

    private void addAction(ActionEvent e) {
        new AddView().setVisible(true);
        this.dispose();
    }

    private void delAction(ActionEvent e) {
        new DelView().setVisible(true);
        this.dispose();
    }

    private void updateAction(ActionEvent e) {
        new RecordView().setVisible(true);
        this.dispose();
    }

    private void searchAction(ActionEvent e) {
        new SearchView().setVisible(true);
        this.dispose();
    }

    private void costAction(ActionEvent actionEvent) {
        new CostView().setVisible(true);
        this.dispose();
    }

    private void roomAction(ActionEvent actionEvent) {
        new RoomView().setVisible(true);
        this.dispose();
    }

    private void spaceAction(ActionEvent actionEvent) {
        new SpaceView().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
