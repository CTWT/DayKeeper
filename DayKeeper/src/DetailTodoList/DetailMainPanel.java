package DetailTodoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;

/**
 * 생성자 : 유연우  
 * 생성일 : 25.05.18  
 * 파일명 : DetailMainPanel.java  
 * 수정자 : 
 * 수정일 :
 * 설명 : 오늘 할일을 보여주고, 입력 및 삭제 화면으로 전환할 수 있는 메인 패널 클래스  
 */
public class DetailMainPanel extends JPanel {

    private final DetailMainFrame frame;

    private JList<String> todoList;
    private JButton inputButton;

    /**
     * 생성자: 메인 패널 UI 초기화 및 이벤트 등록
     *
     * @param frame 부모 프레임 참조
     */
    public DetailMainPanel(DetailMainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initUI();
        initEvent();
    }

    /**
     * 패널 내 UI 컴포넌트 구성
     */
    private void initUI() {
        // 상단 타이틀 라벨 추가
        add(CommonStyle.createTitleLabel(), BorderLayout.NORTH);

        // 할일 리스트 및 스크롤
        todoList = new JList<>(frame.getTodoListModel());
        todoList.setPreferredSize(new Dimension(300, 350));
        JScrollPane scrollPane = new JScrollPane(todoList);
        add(scrollPane, BorderLayout.CENTER);

        // '오늘할일입력' 버튼 설정
        inputButton = new JButton("오늘할일입력");
        CommonStyle.stylePrimaryButton(inputButton);

        // 공통 하단 버튼 (Remove 등 포함) 패널 생성
        BottomPanelComponents bottomComp = CommonStyle.createBottomPanel();

        // 하단 전체 래퍼 패널 구성 (입력 버튼 + 공통 버튼)
        JPanel bottomWrapperPanel = new JPanel(new BorderLayout());

        JPanel inputBtnPanel = new JPanel(); // 가운데 입력 버튼
        inputBtnPanel.add(inputButton);
        bottomWrapperPanel.add(inputBtnPanel, BorderLayout.NORTH);

        bottomWrapperPanel.add(bottomComp.panel, BorderLayout.SOUTH); // 공통 버튼 영역

        add(bottomWrapperPanel, BorderLayout.SOUTH);
    }

    /**
     * 이벤트 등록 (버튼 클릭, 리스트 더블클릭)
     */
    private void initEvent() {
        // '오늘할일입력' 버튼 클릭 시 입력 화면으로 전환
        inputButton.addActionListener((ActionEvent e) -> {
            frame.showPanel(DetailMainFrame.INPUT);
        });

        // 리스트 항목 더블클릭 시 해당 할일 삭제 화면으로 이동
        todoList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedTitle = todoList.getSelectedValue();
                    if (selectedTitle != null) {
                        frame.setSelectedTitle(selectedTitle);
                        frame.showPanel(DetailMainFrame.REMOVE);
                    }
                }
            }
        });
    }
}
