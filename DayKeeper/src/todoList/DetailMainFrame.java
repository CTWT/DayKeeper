package todoList;

import java.awt.CardLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.18
 * 파일명 : DetailMainFrame.java
 * 수정자 : 
 * 수정일 :
 * 설명 : Detail 관련 3개의 창 기본 설정 및 컨트롤을 담당하는 메인 프레임 클래스
 */

public class DetailMainFrame extends JFrame {
    public static final String MAIN = "MainPanel";
    public static final String INPUT = "InputPanel";
    public static final String REMOVE = "RemovePanel";

    private CardLayout cardLayout;
    private JPanel mainContainer;

    // 공유 데이터: 할일 제목 리스트 모델과 할일 내용 맵
    private DefaultListModel<String> todoListModel = new DefaultListModel<>();
    private java.util.Map<String, String> todoContentMap = new java.util.HashMap<>();
    private String selectedTitle = "";

    // 관리할 각 패널
    private DetailMainPanel mainPanel;
    private InputPanel inputPanel;
    private RemovePanel removePanel;

    /**
     * DetailMainFrame 기본 생성자.
     * 프레임 크기 및 종료 동작 설정, 패널 초기화 및 CardLayout 설정을 수행한다.
     */
    public DetailMainFrame() {
        setTitle("DAY-KEEPER");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainPanel = new DetailMainPanel(this);
        inputPanel = new InputPanel(this);
        removePanel = new RemovePanel(this);

        mainContainer.add(mainPanel, MAIN);
        mainContainer.add(inputPanel, INPUT);
        mainContainer.add(removePanel, REMOVE);

        add(mainContainer);
        setVisible(true);
    }

    /**
     * 지정한 이름의 패널을 보여준다.
     * 만약 REMOVE 패널로 전환 시 removePanel의 데이터 업데이트를 호출한다.
     * 
     * @param name 보여줄 패널의 이름 (MainPanel, InputPanel, RemovePanel 중 하나)
     */
    public void showPanel(String name) {
        if (name.equals(REMOVE)) {
            removePanel.updateData();
        }
        cardLayout.show(mainContainer, name);
    }

    /**
     * 할일 제목 리스트 모델을 반환한다.
     * 
     * @return 할일 제목들이 저장된 DefaultListModel<String>
     */
    public DefaultListModel<String> getTodoListModel() {
        return todoListModel;
    }

    /**
     * 할일 제목과 내용이 매핑된 맵을 반환한다.
     * 
     * @return 할일 제목과 내용이 저장된 Map<String, String>
     */
    public java.util.Map<String, String> getTodoContentMap() {
        return todoContentMap;
    }

    /**
     * 현재 선택된 할일 제목을 설정한다.
     * 
     * @param title 선택할 할일 제목
     */
    public void setSelectedTitle(String title) {
        this.selectedTitle = title;
    }

    /**
     * 현재 선택된 할일 제목을 반환한다.
     * 
     * @return 선택된 할일 제목
     */
    public String getSelectedTitle() {
        return selectedTitle;
    }

    /**
     * RemovePanel 인스턴스를 반환한다.
     * 
     * @return RemovePanel 객체
     */
    public RemovePanel getRemovePanel() {
        return removePanel;
    }

    /**
     * 애플리케이션 진입점 메인 메서드.
     * DetailMainFrame 인스턴스를 생성하여 프로그램을 시작한다.
     * 
     * @param args 커맨드라인 인수 (사용하지 않음)
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new DetailMainFrame();
        });
    }

}