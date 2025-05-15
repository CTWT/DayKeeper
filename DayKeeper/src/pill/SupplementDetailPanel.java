package pill;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SupplementDetailPanel extends JPanel {
    private JLabel nameLabel;
    private JLabel imageLabel;
    private JLabel descriptionLabel;
    private SupApp parent;
    private Map<String, String> supplementDescriptions;

    public SupplementDetailPanel(SupApp parent) {
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        nameLabel = new JLabel("약 이름", SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(nameLabel);

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 150));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(245, 245, 245));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel);

        descriptionLabel = new JLabel("<html><div style='text-align:center;'>영양제 설명이 여기에 표시됩니다.</div></html>");
        descriptionLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(descriptionLabel);

        JButton backButton = new JButton("뒤로");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setPreferredSize(new Dimension(80, 30));
        backButton.setMaximumSize(new Dimension(80, 30));
        backButton.addActionListener(e -> parent.showListPanel());
        add(backButton);

        initSupplementDescriptions();
    }

    public void loadSupplementInfo(String name) {
        nameLabel.setText(name);

        ImageIcon icon = new ImageIcon("img/" + name + ".png");
        if (icon.getIconWidth() > 0) {
            Image scaled = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
            imageLabel.setText("");
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("이미지 없음");
        }

        String desc = supplementDescriptions.getOrDefault(name, "해당 약에 대한 설명이 없습니다.");
        descriptionLabel.setText("<html><div style='text-align:center;'>" + desc + "</div></html>");
    }

    private void initSupplementDescriptions() {
        supplementDescriptions = new HashMap<>();
        supplementDescriptions.put("코카인", "중추신경계를 자극하는 강력한 약물입니다.<br>남용 시 심각한 중독과 건강 문제를 유발합니다.");
        supplementDescriptions.put("마리화나", "환각 작용이 있는 대마초에서 추출된 물질입니다.<br>일부 국가에서는 의료용으로 사용됩니다.");
        supplementDescriptions.put("헤로인", "마약성 진통제로, 강력한 중독성을 지닌 약물입니다.");
        // ... 필요시 추가 가능
    }
}
