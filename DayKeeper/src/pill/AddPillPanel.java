package pill;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import common.CommonStyle;
import dbConnection.DBManager;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : 임해균
 * 작성일 : 2025.05.16
 * 파일명 : AddPillPanel.java
 * 설명 : 여러 영양제를 동시에 선택하여 일괄 등록할 수 있는 패널
 *       - 버튼 다중 선택 가능
 *       - 수량은 스피너로 공통 입력
 *       - DB에 한 번에 여러 약 insert 처리
 */


public class AddPillPanel extends JPanel {
    private PillApp parent;
    private JLabel nameLabel;
    private JLabel descLabel;
    private JLabel imageLabel;
    private Set<String> selectedDrugs = new HashSet<>();
    private HashMap<String, JButton> buttonMap = new HashMap<>();

    public AddPillPanel(PillApp parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBackground(CommonStyle.BACKGROUND_COLOR);

        // 상단 제목
        nameLabel = CommonStyle.createTitleLabel();
        nameLabel.setText("영양제를 선택하세요");
        add(nameLabel, BorderLayout.NORTH);

        // 중앙 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        // 이미지
        int width = 200;
        int height = 150;
        imageLabel = new JLabel(createImageIcon("", width, height));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.add(imageLabel);

        // 설명
        descLabel = CommonStyle.createLabel("약 설명이 여기에 표시됩니다.");
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));
        centerPanel.add(descLabel);

        // 버튼 그리드
        JPanel buttonGrid = new JPanel(new GridLayout(4, 3, 10, 10));
        buttonGrid.setBackground(CommonStyle.BACKGROUND_COLOR);
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        String[] drugList = {
            "루테인", "마그네슘", "멀티비타민",
            "비오틴", "비타민C", "비타민D",
            "칼슘", "아연", "오메가3",
            "유산균", "철분", "홍삼"
        };

        for (String drug : drugList) {
            JButton btn = new JButton(drug);
            CommonStyle.stylePrimaryButton(btn);
            btn.setFont(CommonStyle.TEXT_FONT);
            btn.addActionListener(e -> toggleDrugSelection(drug));
            buttonMap.put(drug, btn);
            buttonGrid.add(btn);
        }

        centerPanel.add(buttonGrid);
        add(centerPanel, BorderLayout.CENTER);

        // 하단 패널
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(CommonStyle.BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        bottomPanel.add(CommonStyle.createLabel("수량"));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
        bottomPanel.add(quantitySpinner);

        // 확인 버튼
        JButton confirmBtn = new JButton("확인");
        confirmBtn.setPreferredSize(new Dimension(80, 35));
        CommonStyle.stylePrimaryButton(confirmBtn);
        confirmBtn.addActionListener(e -> {
            if (selectedDrugs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "약을 하나 이상 선택해주세요.");
                return;
            }
            int qty = (int) quantitySpinner.getValue();
            for (String drug : selectedDrugs) {
                insertDrugToDB(drug, qty);
            }
            JOptionPane.showMessageDialog(this, "추가 완료!");
            parent.showPanel("list");
        });
        bottomPanel.add(confirmBtn);

        // 뒤로가기 버튼
        JButton backBtn = new JButton("뒤로");
        backBtn.setPreferredSize(new Dimension(80, 35));
        backBtn.setFont(CommonStyle.TEXT_FONT);
        backBtn.setBackground(Color.LIGHT_GRAY);
        backBtn.addActionListener(e -> parent.showPanel("list"));
        bottomPanel.add(backBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 선택 토글 함수
    private void toggleDrugSelection(String drug) {
        if (selectedDrugs.contains(drug)) {
            selectedDrugs.remove(drug);
            buttonMap.get(drug).setBackground(CommonStyle.PRIMARY_COLOR);
            nameLabel.setText("영양제를 선택하세요");
            imageLabel.setIcon(createImageIcon("", 200, 150));
            descLabel.setText("약 설명이 여기에 표시됩니다.");
        } else {
            selectedDrugs.add(drug);
            buttonMap.get(drug).setBackground(Color.GRAY);
            nameLabel.setText(drug + " 선택됨");
            imageLabel.setIcon(createImageIcon(drug, 200, 150));
            descLabel.setText(PillManager.getInst().getDescription(drug));
        }
    }

    // 이미지 생성
    public ImageIcon createImageIcon(String name, int width, int height) {
        Image image = ResourcesManager.getInst().getImagebyName(name);
        if (image != null) {
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        Image emptyImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return new ImageIcon(emptyImage);
    }

    // DB insert
    public void insertDrugToDB(String pillName, int amount) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "INSERT INTO pill(pill_id, id, pillName, pillDetail, pillAmount, date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, PillManager.nextInt++);
            pstmt.setString(2, "12345");
            pstmt.setString(3, pillName);
            pstmt.setString(4, PillManager.getInst().getDescription(pillName));
            pstmt.setInt(5, amount);
            pstmt.setString(6, LocalDateTime.now().toLocalDate().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
