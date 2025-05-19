package pill;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import pill.pillPanel.Pill;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : ChatGPT
 * 수정일 : 2025.05.16
 * 파일명 : SupApp.java
 * 설명 : 전체 화면을 관리하는 메인 프레임 (CardLayout을 이용한 화면 전환 포함)
 */

public class PillApp extends JFrame {
    // 각 화면 패널
    private Pill listPanel;

    /**
     * 생성자: 프레임 기본 설정 및 초기 화면 설정
     */
    public PillApp() {
        setTitle("daykeeper");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 화면 중앙 배치

        listPanel = new Pill();
        add(listPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PillApp().setVisible(true);
        });
    }
}
