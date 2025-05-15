package pill;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정일 : 2025.05.15
 * 파일명 : AddSupplementPanel.java
 * 설명 : 영양제 상세 보기 및 선택용 패널
 */

public class AddSupplementPanel extends JPanel {
    private SupApp parent;
    private JLabel nameLabel;
    private JComboBox<String> timeComboBox;

    public AddSupplementPanel(SupApp parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // [1] 상단: 약 이름
        nameLabel = new JLabel("비타민D", SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(nameLabel, BorderLayout.NORTH);

        // [2] 중앙 전체 묶음 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // [3] 약 이미지
        JLabel imageLabel = new JLabel(new ImageIcon("img/vitaminD.png"));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(imageLabel);

        // [4] 약 설명
        JLabel descLabel = new JLabel("<html>뼈 건강에 도움을 주는 비타민D 보충제입니다.</html>");
        descLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        centerPanel.add(descLabel);

        // [5] 약 버튼 그리드 (4행 3열)
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
            btn.addActionListener(e -> parent.showPanel("detail")); // 추후 로직 연결 가능
            buttonGrid.add(btn);
        }
        centerPanel.add(buttonGrid);

        add(centerPanel, BorderLayout.CENTER);

        // [6] 하단: 수량, 시간, 확인 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        bottomPanel.add(new JLabel("수량"));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));
        bottomPanel.add(quantitySpinner);

        bottomPanel.add(new JLabel("시간"));
        String[] hours = new String[25];
        for (int i = 0; i <= 24; i++) {
            hours[i] = String.format("%02d시", i);
        }
        timeComboBox = new JComboBox<>(hours);
        timeComboBox.setPreferredSize(new Dimension(80, 30));
        bottomPanel.add(timeComboBox);

        JButton confirmBtn = new JButton("확인");
        confirmBtn.setPreferredSize(new Dimension(70, 35));
        confirmBtn.setBackground(new Color(120, 60, 255));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        bottomPanel.add(confirmBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}
