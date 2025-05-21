package todoDetail;

import java.awt.BorderLayout;
import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
//import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;
import common.Session;
import config.BaseFrame;
import config.ScreenType;
import dbConnection.TodoDAO;
import dbConnection.TodoDTO;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.20
 * 파일명 : TodoDetailPanel.java
 * 수정자 : 
 * 수정일 : 
 * 설명 : Detail 화면을 하나의 JPanel로 구성 - 
 */

public class TodoDetail extends JPanel {

    private List<TodoDTO> todoList;

    // 리스트
    private final DefaultListModel<String> titleListModel = new DefaultListModel<>();
    private final DefaultListModel<String> contentListModel = new DefaultListModel<>();
    private final Map<String, String> todoMap = new LinkedHashMap<>(); // 순서 유지

    public TodoDetail() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        loadData();

        // 상단 타이틀
        JLabel title = CommonStyle.createTitleLabel();
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        // 중앙 영역 패널 - 리스트 패널 (왼쪽: 제목 / 오른쪽: 내용)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(10, 100, 10, 100));

        // 제목 리스트
        JList<String> titleList = new JList<>(titleListModel);
        JScrollPane titleScroll = new JScrollPane(titleList);
        centerPanel.add(titleScroll);

        // // 내용 리스트 (단일 선택된 제목의 내용 보여줌)
        // JList<String> contentList = new JList<>(contentListModel);
        // JScrollPane contentScroll = new JScrollPane(contentList);
        // centerPanel.add(contentScroll);

        add(centerPanel, BorderLayout.CENTER);

        // 하단 버튼 구성
        BottomPanelComponents bottom = CommonStyle.createBottomPanel();

        bottom.todoList.setVisible(true);
        bottom.todoDetailInput.setVisible(true);
        bottom.pillDetail.setVisible(true);
        bottom.statistics.setVisible(true);

        bottom.todoDetailInput.addActionListener(e -> {
            JDialog d = new TodoInput(this);
        });
        bottom.pillDetail.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.PILL);
        });
        bottom.statistics.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.STATISTICS);
        });

        add(bottom.panel, BorderLayout.SOUTH);

        // 제목 더블클릭 시 삭제 다이얼로그
        TodoDetail parent = this;
        titleList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = titleList.getSelectedValue();
                    if (selected != null) {
                        new TodoRemove(parent, selected);
                    }
                }
            }
        });

    }

    // main 테스트용
    public static void main(String[] args) {
        BaseFrame f = new BaseFrame();
        f.setContentPane(new TodoDetail());
        f.setVisible(true);
    }

    public void loadData() {
        if (todoList != null) {
            todoList.clear();
        }
        todoList = TodoDAO.todoList(Session.getUserId());

        Iterator<TodoDTO> iter = todoList.iterator();
        while (iter.hasNext()) {
            TodoDTO dto = iter.next();

            pushData(dto.getTodoTitle(), "detaildetail");
        }
    }

    public void pushData(String title, String content) {
        titleListModel.addElement(title);
        contentListModel.addElement(content);
        todoMap.put(title, content);
    }

    public void deleteData(String title) {
        titleListModel.removeElement(title);
        contentListModel.removeElement(todoMap.get(title));
        todoMap.remove(title);
    }

    public Map<String, String> getTodoMap() {
        return todoMap;
    }

}
