package pill;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SupplementListPanel extends JPanel {
    private SupApp parent;
    private JLabel nameLabel;
    private JLabel photoLabel;
    private JComboBox<String> timeComboBox;

    public SupplementListPanel(SupApp parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ------------------- 상단 뷰어 -------------------
        JPanel viewerPanel = new JPanel();
        viewerPanel.setLayout(new BoxLayout(viewerPanel, BoxLayout.Y_AXIS));
        viewerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        viewerPanel.setBackground(Color.WHITE);

        photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(200, 100));
        photoLabel.setOpaque(true);
        photoLabel.setBackground(new Color(245, 245, 245)); // 밝은 회색
        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        nameLabel = new JLabel("약을 선택해주세요", SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        viewerPanel.add(photoLabel);
        viewerPanel.add(nameLabel);

        // ------------------- 약 버튼 + 타이틀 -------------------
        JLabel selectLabel = new JLabel("추가할 약을 선택하세요", SwingConstants.CENTER);
        selectLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        selectLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel buttonGrid = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 40));
        buttonGrid.setBackground(Color.WHITE);

        String[] drugList = {
            "필로폰", "마리화나",
            "양귀비", "코카인",
            "헤로인", "아편",
            "모르핀", "펜타민"
        };

        for (String drug : drugList) {
            JButton btn = new JButton(drug);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(230, 230, 250));
            btn.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            btn.addActionListener(e -> updateViewer(drug));
            buttonGrid.add(btn);
        }

        // ------------------- 하단: Time + 버튼 -------------------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel timeLabel = new JLabel("Time:");

        String[] hours = new String[25];
        for (int i = 0; i <= 24; i++) {
            hours[i] = String.format("%02d시", i);
        }

        timeComboBox = new JComboBox<>(hours);
        timeComboBox.setPreferredSize(new Dimension(80, 30));

        JButton addBtn = new JButton("...");
        addBtn.setPreferredSize(new Dimension(40, 40));
        addBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        addBtn.setBackground(new Color(120, 60, 255));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);

        addBtn.addActionListener(e -> {
            String selectedDrug = nameLabel.getText().replace(" 선택됨", "");
            String selectedTime = (String) timeComboBox.getSelectedItem();
            JOptionPane.showMessageDialog(this,
                "약: " + selectedDrug + "\n시간: " + selectedTime,
                "등록 확인",
                JOptionPane.INFORMATION_MESSAGE);
        });

        bottomPanel.add(timeLabel);
        bottomPanel.add(timeComboBox);
        bottomPanel.add(addBtn);

        // ------------------- 중앙 내용 조립 -------------------
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(selectLabel);
        centerPanel.add(buttonGrid);
        centerPanel.add(bottomPanel);

        add(viewerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void updateViewer(String name) {
        nameLabel.setText(name + " 선택됨");
        // 추후 이미지 연동 가능
        photoLabel.setBackground(new Color(220, 220, 255)); // 선택 시 색 변경
    }
}
