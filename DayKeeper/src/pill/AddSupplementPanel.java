package pill;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 2025.05.14
 * 파일명 : AddSupplementPanel.java
 */

// 영양제 상세 보기 화면 (데이터베이스에서 가져와 보여주기만 함)
public class AddSupplementPanel extends JPanel {
    private SupApp app;
    private JLabel imageLabel;
    private JTextField nameField;
    private JTextArea descArea;

    public AddSupplementPanel(SupApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // 제목
        JLabel title = new JLabel("daykeeper", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // 중앙 보기 영역
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // 이름
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("영양제 이름"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 25));
        nameField.setEditable(false); // ✅ 입력 불가
        formPanel.add(nameField, gbc);

        // 이미지
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("사진"), gbc);
        gbc.gridx = 1;
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(120, 120));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(imageLabel, gbc);

        // 설명
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("영양제 설명"), gbc);
        gbc.gridx = 1;
        descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false); // ✅ 입력 불가
        JScrollPane scrollPane = new JScrollPane(descArea);
        formPanel.add(scrollPane, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 하단 버튼
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245, 245, 245));
        JButton backBtn = new JButton("🔙 뒤로가기");
        backBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        backBtn.addActionListener(e -> app.showPanel("list"));

        btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // 외부에서 데이터 설정하는 메서드 (필요 시 DB 연동용)
    public void setSupplementInfo(String name, String description, ImageIcon imageIcon) {
        nameField.setText(name);
        descArea.setText(description);
        imageLabel.setIcon(imageIcon);
    }
}
