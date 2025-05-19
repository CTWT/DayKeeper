package pill;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pill.pillPanel.AddPillPanel;
import pill.pillPanel.PillDetailPanel;
import pill.pillPanel.PillListPanel;
import pill.pillPanel.TimeSettingPanel;

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

    // 현재 디테일패널에 나올 영양제의 id를 저장하는 클래스
    private static class DetailInfo {
        public static Integer pillId;
    }

    // 디테일패널에 나올 영양제 id
    public Integer getDetailId() {
        return DetailInfo.pillId;
    }

    // 디테일패널에 나올 영양제 id 세팅
    public void setDetailId(Integer id) {
        DetailInfo.pillId = id;
    }

    private CardLayout cardLayout;   // 화면 전환 레이아웃
    private JPanel mainPanel;        // 전체 화면을 담는 메인 패널

    // 각 화면 패널
    private PillListPanel listPanel;
    private PillDetailPanel detailPanel;
    private AddPillPanel addPanel;
    private TimeSettingPanel timePanel; // ✅ 추가된 시간 설정 패널

    /**
     * 생성자: 프레임 기본 설정 및 초기 화면 설정
     */
    public PillApp() {
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
     * @param name 화면 식별자 ("list", "detail", "add", "time")
     */
    public void showPanel(String name) {
        updatePanel(name);
        cardLayout.show(mainPanel, name);
    }

    /**
     * 화면 이름에 따라 해당 화면 패널을 생성 후 mainPanel에 추가
     * - 이미 추가된 경우 중복 생성 방지
     */
    private void updatePanel(String name) {
        switch (name) {
            case "list":
                listPanel = new PillListPanel(this);
                mainPanel.add(listPanel, "list");
                break;
            case "detail":
                detailPanel = new PillDetailPanel(this);
                mainPanel.add(detailPanel, "detail");
                break;
            case "add":
                addPanel = new AddPillPanel(this);
                mainPanel.add(addPanel, "add");
                break;
            case "time":
                if (timePanel == null) {
                    timePanel = new TimeSettingPanel(this);
                    mainPanel.add(timePanel, "time");
                }
                break;
            default:
                System.err.println("알 수 없는 패널 이름: " + name);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PillApp().setVisible(true);
        });
    }
}
