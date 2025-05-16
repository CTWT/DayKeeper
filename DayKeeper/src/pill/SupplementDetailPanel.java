package pill;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import dbConnection.DBManager;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정일 : 2025.05.16
 * 파일명 : SupplementDetailPanel.java
 * 설명 : 영양제 상세 보기 화면. 이미지, 설명, 복용팁 출력 및 뒤로/삭제 버튼 제공
 */

public class SupplementDetailPanel extends JPanel {
    private SupApp parent;

    /**
     * 생성자 - 상세 보기 화면 구성
     * @param parent 메인 앱 프레임
     */
    public SupplementDetailPanel(SupApp parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        Integer pillId = parent.getDetailId();
        PillDTO dto = PillManager.getInst().getDataById(pillId);
        String pillName = dto.getPillName();
        int amount = getPillAmountFromDB(pillId);

        // [1] 상단 제목
        JLabel titleLabel = new JLabel(pillName + " (" + amount + "개 남음)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        titleLabel.setForeground(new Color(50, 70, 160));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // [2] 중앙 내용
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // 이미지 출력
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

        // 설명 + 팁
        centerPanel.add(makeInfoBox("약 설명", PillManager.getInst().getDescription(pillName)));
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(makeInfoBox("복용 팁", PillManager.getInst().getTip(pillName)));

        add(centerPanel, BorderLayout.CENTER);

        // [3] 하단 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnPanel.setBackground(Color.WHITE);

        JButton backBtn = new JButton("뒤로");
        JButton deleteBtn = new JButton("삭제");

        for (JButton btn : new JButton[]{backBtn, deleteBtn}) {
            btn.setPreferredSize(new Dimension(90, 35));
            btn.setFocusPainted(false);
            btn.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        }

        backBtn.setBackground(new Color(230, 240, 255));
        deleteBtn.setBackground(new Color(255, 230, 230));
        backBtn.addActionListener(e -> parent.showPanel("list"));

        btnPanel.add(backBtn);
        btnPanel.add(deleteBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    /**
     * 설명/복용 팁을 예쁘게 감싸는 박스 생성
     * @param title 박스 제목
     * @param content 박스 내용
     * @return JPanel 박스
     */
    private JPanel makeInfoBox(String title, String content) {
        if (content == null) content = "정보 없음";

        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(Color.WHITE);
        box.setMaximumSize(new Dimension(500, 90));

        JTextArea text = new JTextArea(content);
        text.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setBackground(new Color(248, 248, 248));
        text.setBorder(new EmptyBorder(10, 15, 10, 15));

        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            title, TitledBorder.LEFT, TitledBorder.TOP,
            new Font("맑은 고딕", Font.BOLD, 13), new Color(80, 80, 80)
        );

        box.setBorder(border);
        box.add(text, BorderLayout.CENTER);
        return box;
    }

    /**
     * DB에서 영양제 수량 가져오기
     * @param pillId 영양제 ID
     * @return 수량(int)
     */
    private int getPillAmountFromDB(Integer pillId) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT pillAmount FROM pill WHERE pill_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, pillId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("pillAmount");
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
