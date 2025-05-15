package pill;

import javax.swing.*;
import java.awt.*;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 25.05.14
 * 파일명 : AddSupplementPanel.java
 * 설명 : 영양제 등록 또는 상세 보기 패널
 */

public class AddSupplementPanel extends JPanel {
    private JLabel nameLabel;
    private JLabel descriptionLabel;

    public AddSupplementPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        nameLabel = new JLabel("영양제 이름");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        descriptionLabel = new JLabel("상세 설명이 여기에 표시됩니다.");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backButton = new JButton("뒤로가기");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame instanceof SupApp) {
                ((SupApp) topFrame).showListPanel();
            }
        });

        add(nameLabel);
        add(Box.createVerticalStrut(10));
        add(descriptionLabel);
        add(Box.createVerticalStrut(20));
        add(backButton);
    }

    public void loadSupplementInfo(String name) {
        if (name == null) {
            nameLabel.setText("새로운 영양제 등록");
            descriptionLabel.setText("이곳에 등록할 내용을 표시하거나 입력할 수 있습니다.");
        } else {
            nameLabel.setText("영양제: " + name);
            descriptionLabel.setText(name + "의 상세 정보를 표시합니다.");
        }
    }
}
