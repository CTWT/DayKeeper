package DetailTodoList;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 생성자 : 유연우  
 * 생성일 : 25.05.18  
 * 파일명 : DetailMainFrame.java  
 * 수정자 : 
 * 수정일 :
 * 설명 : 할일 관리에서 세 가지 화면(Main, Input, Remove)을 전환하고 관리하는 메인 프레임 클래스  
 */
public class DetailMainFrame extends JFrame {

    // 패널 구분을 위한 문자열 상수
    public static final String MAIN = "MainPanel";
    public static final String INPUT = "InputPanel";
    public static final String REMOVE = "RemovePanel";

    // CardLayout을 활용한 패널 전환용 레이아웃 및 컨테이너
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // 할일 제목과 내용을 저장하는 데이터 모델
    private DefaultListModel<String> todoListModel = new DefaultListModel<>(); // 제목 리스트
    private Map<String, String> todoContentMap = new HashMap<>();              // 제목-내용 매핑
    private String selectedTitle = "";                                         // 선택된 제목

    // 세 가지 주요 패널
    private DetailMainPanel mainPanel;
    private InputPanel inputPanel;
    private RemovePanel removePanel;

    /**
     * 생성자: 프레임 기본 설정 및 패널 초기화 수행
     */
    public DetailMainFrame() {
        setTitle("DAY-KEEPER");                      // 윈도우 제목
        setSize(800, 600);                           // 기본 크기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫기 버튼 클릭 시 종료
        setLocationRelativeTo(null);                 // 화면 중앙 정렬

        // 레이아웃 및 컨테이너 설정
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 패널 초기화 및 등록
        initPanels();

        // 컨테이너를 프레임에 추가하고 표시
        add(mainContainer);
        setVisible(true);
    }

    /**
     * 모든 화면 패널을 생성하고 mainContainer에 등록하는 메서드
     */
    private void initPanels() {
        mainPanel = new DetailMainPanel(this);
        inputPanel = new InputPanel(this);
        removePanel = new RemovePanel(this);

        mainContainer.add(mainPanel, MAIN);
        mainContainer.add(inputPanel, INPUT);
        mainContainer.add(removePanel, REMOVE);
    }

    /**
     * 패널 전환 메서드
     * REMOVE 패널로 이동 시, 최신 데이터로 업데이트하도록 설정
     *
     * @param name 전환할 패널 이름
     */
    public void showPanel(String name) {
        if (REMOVE.equals(name)) {
            removePanel.updateData(); // 삭제 목록을 새로고침
        }
        cardLayout.show(mainContainer, name); // 지정된 이름의 패널 표시
    }

    /**
     * 할일 제목 리스트 모델 반환
     *
     * @return DefaultListModel<String> 형태의 제목 리스트
     */
    public DefaultListModel<String> getTodoListModel() {
        return todoListModel;
    }

    /**
     * 할일 제목-내용을 저장한 맵 반환
     *
     * @return Map<String, String> 형태의 할일 정보
     */
    public Map<String, String> getTodoContentMap() {
        return todoContentMap;
    }

    /**
     * 선택된 할일 제목을 설정
     *
     * @param title 사용자가 선택한 제목
     */
    public void setSelectedTitle(String title) {
        this.selectedTitle = title;
    }

    /**
     * 현재 선택된 할일 제목 반환
     *
     * @return 선택된 제목 문자열
     */
    public String getSelectedTitle() {
        return selectedTitle;
    }

    /**
     * RemovePanel 인스턴스 반환
     * (RemovePanel에서 직접 메서드 호출이 필요할 경우 사용)
     *
     * @return RemovePanel 객체
     */
    public RemovePanel getRemovePanel() {
        return removePanel;
    }

    /**
     * 메인 메서드: 애플리케이션 실행 진입점
     *
     * @param args 커맨드라인 인자 (사용하지 않음)
     */
    public static void main(String[] args) {
        // UI 생성은 이벤트 디스패치 스레드에서 실행해야 함
        javax.swing.SwingUtilities.invokeLater(DetailMainFrame::new);
    }
}
