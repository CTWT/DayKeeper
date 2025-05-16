package pill;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.swing.*;

import dbConnection.DBManager;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : ChatGPT
 * 수정일 : 2025.05.16
 * 파일명 : AddSupplementPanel.java
 * 설명 : 영양제 선택 및 추가 패널 - 시간 선택 제거, 뒤로가기 버튼 추가
 */

public class AddSupplementPanel extends JPanel {
    private SupApp parent;
    private JLabel nameLabel;
    private String selectedPill = null;

    public AddSupplementPanel(SupApp parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // [1] 상단 제목
        nameLabel = new JLabel("약을 선택하세요", SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(nameLabel, BorderLayout.NORTH);

        // [2] 중앙 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // [3] 기본 이미지
        int width = 200;
        int height = 150;
        ImageIcon imageIcon = createImageIcon("", width, height);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(imageLabel);

        // [4] 약 설명
        JLabel descLabel = new JLabel("<html>뼈 건강에 도움을 주는 비타민D 보충제입니다.</html>");
        descLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        centerPanel.add(descLabel);

        // [5] 영양제 선택 버튼 (4행 3열)
        JPanel buttonGrid = new JPanel(new GridLayout(4, 3, 10, 10));
        buttonGrid.setBackground(Color.WHITE);
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        String[] drugList = {
            "루테인", "마그네슘", "멀티비타민",
            "비오틴", "비타민C", "비타민D",
            "칼슘", "아연", "오메가3",
            "유산균", "철분", "홍삼"
        };

        for (String drug : drugList) {
            JButton btn = new JButton(drug);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(230, 230, 250));
            btn.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            btn.addActionListener(e -> {
                nameLabel.setText(drug);
                descLabel.setText(PillManager.getInst().getDescription(drug));
                imageLabel.setIcon(createImageIcon(drug, width, height));
                selectedPill = drug;
            });
            buttonGrid.add(btn);
        }
        centerPanel.add(buttonGrid);
        add(centerPanel, BorderLayout.CENTER);

        // [6] 하단 패널: 수량 + 확인 버튼 + 뒤로가기
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        bottomPanel.add(new JLabel("수량"));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));
        bottomPanel.add(quantitySpinner);

        // 확인 버튼
        JButton confirmBtn = new JButton("확인");
        confirmBtn.setPreferredSize(new Dimension(70, 35));
        confirmBtn.setBackground(new Color(120, 60, 255));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        confirmBtn.addActionListener(e -> {
            addPill((int) quantitySpinner.getValue());
            parent.showPanel("list");
        });
        bottomPanel.add(confirmBtn);

        // 뒤로가기 버튼 추가
        JButton backBtn = new JButton("뒤로");
        backBtn.setPreferredSize(new Dimension(70, 35));
        backBtn.setBackground(Color.LIGHT_GRAY);
        backBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        backBtn.addActionListener(e -> parent.showPanel("list"));
        bottomPanel.add(backBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * 이미지 아이콘 생성 (없으면 빈 이미지)
     */
    public ImageIcon createImageIcon(String name, int width, int height) {
        Image image = ResourcesManager.getInst().getImagebyName(name);
        if (image != null) {
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        Image emptyImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return new ImageIcon(emptyImage);
    }

    /**
     * 선택된 영양제 정보를 DB에 삽입
     */
    public void addPill(int amount) {
        String pillName = selectedPill;
        try (Connection con = DBManager.getConnection()) {
            String sql = "INSERT INTO pill(pill_id, id, pillName, pillDetail, pillAmount, date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, PillManager.nextInt++);
            pstmt.setString(2, "12345");
            pstmt.setString(3, pillName);
            pstmt.setString(4, PillManager.getInst().getDescription(pillName));
            pstmt.setInt(5, amount);

            // 현재 날짜만 저장
            String date = LocalDateTime.now().toLocalDate().toString();
            pstmt.setString(6, date);

            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
