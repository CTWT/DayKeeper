package pill;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : 김관호
 * 수정일 : 2025.05.15
 * 파일명 : SupplementDetailPanel.java
 * 설명 : 영양제를 클릭했을 때 디테일정보들이 나오는 패널
 */

/**
 * SupplementDetailPanel
 * 영양제 상세 정보를 표시하는 패널
 */
public class SupplementDetailPanel extends JPanel {
    private JLabel nameLabel;                // 영양제 이름 라벨
    private JLabel imageLabel;               // 영양제 이미지 라벨
    private JLabel descriptionLabel;         // 영양제 설명 라벨
    private SupApp parent;                   // 상위 애플리케이션 참조
    private Map<String, String> supplementDescriptions; // 영양제 설명 저장 맵

    /**
     * 생성자: 패널 초기화
     * @param parent 상위 애플리케이션
     */
    public SupplementDetailPanel(SupApp parent) {
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        // 이름 라벨 초기화
        PillDTO dto = PillManager.getInst().getDataById(parent.getDetailId());
        String pillName = dto.getPillName();

        nameLabel = new JLabel(pillName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(nameLabel);

        try {
            // 이미지 불러오기
            Image image = ResourcesManager.getInst().getImagebyName(pillName);
            // 크기 조정
            Image scaledImage = image.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            // 다시 ImageIcon으로 감싸기
            JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

            add(iconLabel);
        } catch (Exception e) {
            System.out.println("이미지 로드 오류: " + e.getMessage());
            imageLabel.setText("이미지 없음");
        }

        // 설명 라벨 초기화
        String description = PillManager.getInst().getDescription(pillName);
        descriptionLabel = new JLabel("<html><div style='text-align:center;'>" + description + "</div></html>");
        descriptionLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(descriptionLabel);

        // 뒤로가기 버튼 초기화
        JButton backButton = new JButton("뒤로");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setPreferredSize(new Dimension(80, 30));
        backButton.setMaximumSize(new Dimension(80, 30));

        // 뒤로가기 버튼 클릭 시 패널 전환
        backButton.addActionListener(e -> parent.showPanel("list"));
        add(backButton);
    }

    /**
     * 영양제 정보 로드 메서드
     * @param name 영양제 이름
     */
    public void loadSupplementInfo(String name) {
        // 이름 라벨에 영양제 이름 설정
        nameLabel.setText(name);

        // 이미지 로드
        ImageIcon icon = new ImageIcon("img/" + name + ".png");
        if (icon.getIconWidth() > 0) {
            // 이미지 크기 조정
            Image scaled = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
            imageLabel.setText(""); // 텍스트 제거
        } else {
            // 이미지가 없는 경우
            imageLabel.setIcon(null);
            imageLabel.setText("이미지 없음");
        }

        // 설명 설정
        String desc = supplementDescriptions.getOrDefault(name, "해당 약에 대한 설명이 없습니다.");
        descriptionLabel.setText("<html><div style='text-align:center;'>" + desc + "</div></html>");
    }
}
