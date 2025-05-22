package pill.pillPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import common.CommonStyle;
import config.ImgConfig;
import dbConnection.PillDAO;
import dbConnection.PillDTO;
import pill.pillManager.PillManager;

public class PillDetailDialog extends JDialog {

    public PillDetailDialog(Pill parent) {
        setLayout(new BorderLayout());
        setBackground(CommonStyle.BACKGROUND_COLOR);
        setSize(550, 600);

        // ✅ Null 방어 로직 추가
        Integer pillId = parent.getDetailedId();
        if (pillId == null) {
            JOptionPane.showMessageDialog(this, "잘못된 영양제 정보입니다.");
            dispose();
            return;
        }

        PillDTO dto = PillManager.getInst().getDataById(pillId);
        if (dto == null) {
            JOptionPane.showMessageDialog(this, "해당 영양제 데이터를 찾을 수 없습니다.");
            dispose();
            return;
        }

        String pillName = dto.getPillName();
        int amount = new PillDAO().getPillAmount(pillId);

        // 상단 제목
        JLabel titleLabel = CommonStyle.createTitleLabel();
        titleLabel.setText("💊 " + pillName + " (" + amount + "개 남음)");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 중앙 내용
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        // 이미지
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            Image img = ImgConfig.imgComponent("pill/" + pillName);
            imageLabel.setIcon(new ImageIcon(img.getScaledInstance(200, 150, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            imageLabel.setText("이미지 없음");
        }

        imageLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(5, 5, 5, 5)
        ));

        centerPanel.add(imageLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // 설명 / 팁
        centerPanel.add(makeInfoBox("약 설명", PillManager.getInst().getDescription(pillName)));
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(makeInfoBox("복용 팁", PillManager.getInst().getTip(pillName)));

        add(centerPanel, BorderLayout.CENTER);

        // 하단 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        JButton backBtn = new JButton("뒤로");
        CommonStyle.stylePrimaryButton(backBtn);
        backBtn.setPreferredSize(new Dimension(90, 35));
        backBtn.addActionListener(e -> dispose());

        JButton deleteBtn = new JButton("삭제");
        CommonStyle.styleDeleteButton(deleteBtn);
        deleteBtn.addActionListener(e -> {
            new PillDAO().deleteDataById(pillId);
            parent.update();
            dispose();
        });

        btnPanel.add(backBtn);
        btnPanel.add(deleteBtn);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private JPanel makeInfoBox(String title, String content) {
        if (content == null || content.trim().isEmpty()) content = "정보 없음";

        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(CommonStyle.BACKGROUND_COLOR);
        box.setMaximumSize(new Dimension(500, 100));

        JTextArea text = new JTextArea(content);
        text.setFont(CommonStyle.TEXT_FONT);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setBackground(new Color(248, 248, 248));
        text.setBorder(new EmptyBorder(10, 15, 10, 15));

        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            title, TitledBorder.LEFT, TitledBorder.TOP,
            CommonStyle.BUTTON_FONT, new Color(80, 80, 80)
        );

        box.setBorder(border);
        box.add(text, BorderLayout.CENTER);
        return box;
    }
}
