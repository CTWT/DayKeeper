package todoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.18
 * 파일명 : DetailMainPanel.java
 * 수정자 : 
 * 수정일 :
 * 설명 : todolist Detail 메인 창
 */

public class DetailMainPanel extends JPanel {

    private DetailMainFrame frame;

    public DetailMainPanel(DetailMainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 타이틀
        add(CommonStyle.createTitleLabel(), BorderLayout.NORTH);

        // 리스트 및 스크롤
        JList<String> todoList = new JList<>(frame.getTodoListModel());
        JScrollPane scrollPane = new JScrollPane(todoList);
        todoList.setPreferredSize(new Dimension(380, 350));
        add(scrollPane, BorderLayout.CENTER);

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
        inputButton.addActionListener(e -> {
            frame.showPanel(DetailMainFrame.INPUT);
        });

        // 리스트 더블클릭 이벤트
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
