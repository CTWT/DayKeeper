package todoDetail;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.w3c.dom.events.MouseEvent;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.18
 * 파일명 : TodoDetail.java
 * 수정자 : 
 * 수정일 :
 * 설명 : Detail 관련 3개의 창 기본 설정 및 컨트롤을 담당하는 메인 프레임 클래스
 */

public class TodoDetail extends JFrame {
    // 관리할 각 패널
    private DetailMain mainPanel;


    /**
     * DetailMainFrame 기본 생성자.
     * 프레임 크기 및 종료 동작 설정, 패널 초기화 및 CardLayout 설정을 수행한다.
     */
    public TodoDetail() {
        CommonStyle.createTitleLabel();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new DetailMain();

        add(mainPanel);
        setVisible(true);
    }

    // main
    public class DetailMain extends JPanel {

    public DetailMain() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        new TodoDetailDAO().loadTodoByUser("12345");

        // 상단 타이틀
        add(CommonStyle.createTitleLabel(), BorderLayout.NORTH);

        // 리스트 및 스크롤
        JList<String> todoList = new JList<>(TodoDetailManager.getInst().getTodoListModel());
        JScrollPane scrollPane = new JScrollPane(todoList);
        todoList.setPreferredSize(new Dimension(300, 250));
        add(scrollPane, BorderLayout.CENTER);
        //new EmptyBorder(10, 100, 10, 100)
        // JPanel centerPanel = new JPanel(new BorderLayout());
        // centerPanel.setBackground(Color.WHITE);
        // centerPanel.setBorder(new EmptyBorder(10, 100, 10, 100));

        // '오늘할일입력' 버튼
        JButton inputButton = new JButton("오늘할일입력");
        CommonStyle.stylePrimaryButton(inputButton);

        // 하단 공통 버튼 패널
        BottomPanelComponents bottomComp = CommonStyle.createBottomPanel();

        // 하단 버튼 래퍼
        JPanel bottomWrapperPanel = new JPanel(new BorderLayout());
        
        // inputButton 넣는 패널
        JPanel inputBtnPanel = new JPanel();
        inputBtnPanel.add(inputButton);
        bottomWrapperPanel.add(inputBtnPanel, BorderLayout.NORTH);

        // 공통 하단 버튼들
        bottomWrapperPanel.add(bottomComp.panel, BorderLayout.SOUTH);

        add(bottomWrapperPanel, BorderLayout.SOUTH);

        

        // 버튼 클릭 이벤트
        inputButton.addActionListener(e -> { // 할일입력버튼
            //frame.showPanel(DetailMainFrame.INPUT);
            JDialog dialog = new Input();
        });

        // 리스트 더블클릭 이벤트
        todoList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedTitle = todoList.getSelectedValue();
                    Remove dialog = new Remove();
                    dialog.updateData(selectedTitle);
                    // if (selectedTitle != null) {
                    //     DetailTodoManager.getInst().setSelectedTitle(selectedTitle);
                    //     //frame.showPanel(DetailMainFrame.REMOVE);
        
                    // }
                }
            }
        });
    }
}

// manager
public class TodoDetailManager {
    private static TodoDetailManager instance;

    // 공유 데이터: 할일 제목 리스트 모델과 할일 내용 맵
    private DefaultListModel<String> todoListModel = new DefaultListModel<>();
    private java.util.Map<String, String> todoContentMap = new java.util.HashMap<>();
    private String selectedTitle = "";

    private TodoDetailManager(){

    }

    public static TodoDetailManager getInst(){
        if(instance == null) {
            instance = new TodoDetailManager();
        }

        return instance;
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


}



    /**
     * 애플리케이션 진입점 메인 메서드.
     * DetailMainFrame 인스턴스를 생성하여 프로그램을 시작한다.
     * 
     * @param args 커맨드라인 인수 (사용하지 않음)
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TodoDetail();
        });
    }

}

// Detailtodolist -> todoDetail 패키지
// TodoDetailMain 만들기 -> todoDetail.java
// UI 더더더 이쁘게 제작하기! - 리스트에 줄 긋기

