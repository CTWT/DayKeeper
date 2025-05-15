package pill;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 2025.05.15
 * 파일명 : SupApp.java
 * 설명 : 전체 화면을 관리하는 메인 프레임 (CardLayout을 이용한 화면 전환 포함)
 */
public class SupApp extends JFrame {

    private CardLayout cardLayout;   // 화면 전환 레이아웃
    private JPanel mainPanel;         // 전체 화면을 담는 메인 패널

    // 각 화면 패널
    private SupplementListPanel listPanel;
    private SupplementDetailPanel detailPanel;
    private AddSupplementPanel addPanel;

    /**
     * 생성자: 프레임 기본 설정 및 초기 화면 설정
     */
    public SupApp() {
        setTitle("daykeeper");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 화면 중앙 배치

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        add(mainPanel);

        // 초기 화면은 리스트 화면으로 설정
        updatePanel("list");
        cardLayout.show(mainPanel, "list");
    }

    /**
     * 특정 이름의 화면으로 전환
     * @param name 화면 식별자 ("list", "detail", "add")
     */
    public void showPanel(String name) {
        updatePanel(name);      // 패널 초기화 또는 갱신
        cardLayout.show(mainPanel, name);  // 화면 전환
    }

    /**
     * 화면 이름에 따라 해당 화면 패널을 생성 후 mainPanel에 추가
     * - 이미 추가된 경우 계속 새로 추가하는 문제가 있음 (개선 필요)
     * @param name 화면 식별자
     */
    private void updatePanel(String name) {
        switch (name) {
            case "list":
                listPanel = new SupplementListPanel(this);
                mainPanel.add(listPanel, "list");
                break;
            case "detail":
                detailPanel = new SupplementDetailPanel(this);
                mainPanel.add(detailPanel, "detail");
                break;
            case "add":
                addPanel = new AddSupplementPanel(this);
                mainPanel.add(addPanel, "add");
                break;
            default:
                System.err.println("알 수 없는 패널 이름: " + name);
        }
    }

    /**
     * 메인 메서드 - Swing UI 스레드에서 실행
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SupApp().setVisible(true);
        });
    }

    /**
     * 상세보기 패널에 전달할 영양제 ID를 반환
     * @return 현재 상세보기 대상 영양제 ID
     */
    public Integer getDetailId() {
        return DetailInfo.pillId;
    }

    /**
     * 상세보기 패널에 전달할 영양제 ID를 설정
     * @param id 상세보기 대상 영양제 ID
     */
    public void setDetailId(Integer id) {
        DetailInfo.pillId = id;
    }

    /**
     * 상세보기 대상 영양제 ID를 저장하는 정적 중첩 클래스
     */
    private static class DetailInfo {
        public static Integer pillId;
    }
}
