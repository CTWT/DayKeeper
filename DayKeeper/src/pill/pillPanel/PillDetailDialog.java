package pill.pillPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import common.CommonStyle;
import config.ImgConfig;
import dbConnection.PillDAO;
import dbConnection.PillDTO;
import pill.pillManager.PillManager;

import java.awt.*;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 2025.05.16
 * 수정자 : 김관호
 * 수정일 : 2025.05.19
 * 파일명 : PillDetailDialog.java
 * 설명 : 영양제 상세 정보 보기 및 삭제 기능 제공
 */

public class PillDetailDialog extends JDialog {

    public PillDetailDialog(Pill parent) {
        setLayout(new BorderLayout());
        setBackground(CommonStyle.BACKGROUND_COLOR);
        setSize(550, 600);

        PillDTO dto = PillManager.getInst().getDataById(parent.getDetailId());
        String pillName = dto.getPillName();
        int amount = new PillDAO().getPillAmount(parent.getDetailId());

        // 상단 제목
        JLabel titleLabel = CommonStyle.createTitleLabel();
        titleLabel.setText(pillName + " (" + amount + "개 남음)");
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
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        centerPanel.add(imageLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // 설명 / 팁
        centerPanel.add(makeInfoBox("약 설명", PillManager.getInst().getDescription(pillName)));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(makeInfoBox("복용 팁", PillManager.getInst().getTip(pillName)));

        add(centerPanel, BorderLayout.CENTER);

        // 하단 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        JButton backBtn = new JButton("뒤로");
        JButton deleteBtn = new JButton("삭제");

        CommonStyle.stylePrimaryButton(backBtn);
        backBtn.setPreferredSize(new Dimension(90, 35));
        backBtn.addActionListener(e -> dispose());

        CommonStyle.styleDeleteButton(deleteBtn);
        deleteBtn.addActionListener(e -> {
            deleteData(parent);
            parent.update();
            dispose();
        });

        btnPanel.add(backBtn);
        btnPanel.add(deleteBtn);

        add(btnPanel, BorderLayout.SOUTH);
    }

    /**
     * 설명/복용 팁 박스 생성
     *
     * @param title   제목
     * @param content 내용
     */
    private JPanel makeInfoBox(String title, String content) {
        if (content == null)
            content = "정보 없음";

        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(CommonStyle.BACKGROUND_COLOR);
        box.setMaximumSize(new Dimension(500, 90));

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
                CommonStyle.BUTTON_FONT, new Color(80, 80, 80));

        box.setBorder(border);
        box.add(text, BorderLayout.CENTER);
        return box;
    }

    /**
     * 현재 디테일 패널에 대한 영양제를 삭제합니다.
     *
     * @param parent 부모 패널
     */
    private void deleteData(Pill parent) {
        new PillDAO().deleteDataById(parent.getDetailId());
    }
}
