package pill;

import javax.swing.*;
import java.awt.*;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : 
 * 작성일 : 2025.05.14
 * 파일명 : SupplementDetailPanel.java
 */

// 영양제 상세 정보를 표시하는 패널 클래스
public class SupplementDetailPanel extends JPanel {
    private SupApp app; // 메인 프레임 참조

    // 생성자: 메인 프레임 참조를 받아서 화면 전환에 사용
    public SupplementDetailPanel(SupApp app) {
        this.app = app;
        setLayout(new BorderLayout());

        // 중앙에 표시할 라벨 (임시 텍스트)
        JLabel label = new JLabel("영양제 상세 화면", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        // 하단 '뒤로가기' 버튼
        JButton backButton = new JButton("뒤로가기");
        backButton.addActionListener(e -> app.showPanel("list"));
        add(backButton, BorderLayout.SOUTH);
    }
}
