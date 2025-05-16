package pill;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dbConnection.DBManager;

public class SupplementDetailPanel extends JPanel {
    private JLabel nameLabel;
    private JLabel imageLabel = new JLabel(); // null 방지용
    private JLabel descriptionLabel;
    private SupApp parent;

    public SupplementDetailPanel(SupApp parent) {
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        // 영양제 정보 가져오기
        PillDTO dto = PillManager.getInst().getDataById(parent.getDetailId());
        String pillName = dto.getPillName();
        int amount = getPillAmountFromDB(parent.getDetailId());

        // 이름 + 수량 표시
        nameLabel = new JLabel(pillName + " (" + amount + "개 남음)", SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(nameLabel);

        // 이미지 표시
        try {
            Image image = ResourcesManager.getInst().getImagebyName(pillName);
            Image scaledImage = image.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.out.println("이미지 로드 오류: " + e.getMessage());
            imageLabel.setText("이미지 없음");
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(imageLabel);

        // 설명 표시
        String description = PillManager.getInst().getDescription(pillName);
        descriptionLabel = new JLabel("<html><div style='text-align:center;'>" + description + "</div></html>");
        descriptionLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(descriptionLabel);

        // 버튼 패널 (뒤로 + 삭제)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(200, 40));

        JButton backButton = new JButton("뒤로");
        backButton.setPreferredSize(new Dimension(80, 30));
        backButton.addActionListener(e -> parent.showPanel("list"));
        buttonPanel.add(backButton);

        JButton deleteButton = new JButton("삭제");
        deleteButton.setPreferredSize(new Dimension(80, 30));
        // 삭제 기능은 아직 없음
        buttonPanel.add(deleteButton);

        add(Box.createVerticalStrut(10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(buttonPanel);
    }

    // DB에서 수량 가져오는 메서드
    private int getPillAmountFromDB(Integer pillId) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT pillAmount FROM pill WHERE pill_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, pillId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("pillAmount");
            }
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void loadSupplementInfo(String name) {
        // 여기서는 나중에 필요한 경우 동적으로 재로딩할 때 사용할 수 있음
    }
}
