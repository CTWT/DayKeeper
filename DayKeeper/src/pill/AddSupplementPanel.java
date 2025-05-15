package pill;

import javax.swing.*;
import java.awt.*;

public class AddSupplementPanel extends JPanel {
    private SupApp parent;
    private JLabel nameLabel;
    private JComboBox<String> timeComboBox;

    public AddSupplementPanel(SupApp parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 약 선택 안내
        nameLabel = new JLabel("약을 선택해주세요", SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(nameLabel, BorderLayout.NORTH);

        // 약 버튼 그리드
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
            btn.addActionListener(e -> parent.showPanel("detail"));  // 연동!
            buttonGrid.add(btn);
        }

        // 하단 Time + 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel timeLabel = new JLabel("Time:");
        String[] hours = new String[25];
        for (int i = 0; i <= 24; i++) hours[i] = String.format("%02d시", i);
        timeComboBox = new JComboBox<>(hours);
        timeComboBox.setPreferredSize(new Dimension(80, 30));

        JButton addBtn = new JButton("...");
        addBtn.setPreferredSize(new Dimension(40, 40));
        addBtn.setBackground(new Color(120, 60, 255));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        bottomPanel.add(timeLabel);
        bottomPanel.add(timeComboBox);
        bottomPanel.add(addBtn);

        // 조립
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        JLabel selectLabel = new JLabel("추가할 약을 선택하세요", SwingConstants.CENTER);
        selectLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        selectLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        centerPanel.add(selectLabel);
        centerPanel.add(buttonGrid);
        centerPanel.add(bottomPanel);

        add(centerPanel, BorderLayout.CENTER);
    }
}
