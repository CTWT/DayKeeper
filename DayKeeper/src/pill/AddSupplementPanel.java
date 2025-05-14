package pill;

import javax.swing.*;
import java.awt.*;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : 
 * 작성일 : 2025.05.14
 * 파일명 : AddSupplementPanel.java
 */

// 영양제를 등록하는 화면 패널 클래스
public class AddSupplementPanel extends JPanel {
    private SupApp app; // 메인 프레임 참조 (화면 전환에 사용)

    // 생성자: SupApp 참조를 받아 화면 전환 처리 가능하게 함
    public AddSupplementPanel(SupApp app) {
        this.app = app;
        setLayout(new BorderLayout());

        // 중앙 라벨: 현재 화면이 추가화면이라는 안내용
        JLabel label = new JLabel("영양제 추가 화면", SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        add(label, BorderLayout.CENTER);

        // 하단 '뒤로가기' 버튼
        JButton backButton = new JButton("뒤로가기");
        backButton.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        backButton.addActionListener(e -> app.showPanel("list"));
        add(backButton, BorderLayout.SOUTH);
    }
}
