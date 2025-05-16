package pill;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

import common.CommonStyle;
import dbConnection.DBManager;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정일 : 2025.05.16
 * 파일명 : PillDetailPanel.java
 * 설명 : 영양제 상세 보기 화면. 이미지, 설명, 복용팁 출력 및 뒤로/삭제 버튼 제공
 */

public class PillDetailPanel extends JPanel {
    private PillApp parent;

    public PillDetailPanel(PillApp parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(CommonStyle.BACKGROUND_COLOR);

        Integer pillId = parent.getDetailId();
        PillDTO dto = PillManager.getInst().getDataById(pillId);
        String pillName = dto.getPillName();
        int amount = getPillAmountFromDB(pillId);

        // 상단 제목
        JLabel titleLabel = new JLabel(pillName + " (" + amount + "개 남음)", SwingConstants.CENTER);
        titleLabel.setFont(CommonStyle.TITLE_FONT);
        titleLabel.setForeground(CommonStyle.PRIMARY_COLOR);
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
            Image img = ResourcesManager.getInst().getImagebyName(pillName);
            imageLabel.setIcon(new ImageIcon(img.getScaledInstance(200, 150, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            imageLabel.setText("이미지 없음");
        }
        imageLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
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
        backBtn.addActionListener(e -> parent.showPanel("list"));

        deleteBtn.setPreferredSize(new Dimension(90, 35));
        deleteBtn.setFont(CommonStyle.TEXT_FONT);
        deleteBtn.setBackground(new Color(255, 230, 230));
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e->{
            deleteData(parent);
            parent.showPanel("list");
        });

        btnPanel.add(backBtn);
        btnPanel.add(deleteBtn);

        add(btnPanel, BorderLayout.SOUTH);
    }

    /**
     * 설명/복용 팁 박스 생성
     */
    private JPanel makeInfoBox(String title, String content) {
        if (content == null) content = "정보 없음";

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
            CommonStyle.BUTTON_FONT, new Color(80, 80, 80)
        );

        box.setBorder(border);
        box.add(text, BorderLayout.CENTER);
        return box;
    }

    /**
     * DB에서 수량 조회
     */
    private int getPillAmountFromDB(Integer pillId) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT pillAmount FROM pill WHERE pill_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, pillId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("pillAmount");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void deleteData(PillApp parent){
        PillManager.getInst().deleteDataById(parent.getDetailId());
    }
}
