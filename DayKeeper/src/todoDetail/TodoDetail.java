package todoDetail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
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
    // private final DefaultListModel<String> titleListModel = new
    // DefaultListModel<>();
    // private final DefaultListModel<String> contentListModel = new
    // DefaultListModel<>();

    private final DefaultListModel<TodoDTO> titleList = new DefaultListModel<>();

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
        JList<TodoDTO> tList = new JList<>(titleList);
        JScrollPane titleScroll = new JScrollPane(tList);
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
            JDialog todoinput = new TodoInput(this);
        });

        bottom.todoList.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.TODOLIST);
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

        tList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TodoDTO selectedDTO = tList.getSelectedValue();
                    if (selectedDTO != null) {
                        new TodoRemove(parent, selectedDTO.getTodo_id(), selectedDTO.getTodoTitle());
                    }
                }
            }
        });

    }

    // 초기화면 생성 ?
    public void loadData() {
        if (todoList != null) {
            todoList.clear();
        }
        todoList = TodoDAO.todoList();

        Iterator<TodoDTO> iter = todoList.iterator();
        while (iter.hasNext()) {
            TodoDTO dto = iter.next();

            addTodoToView(dto.getTodoTitle(), dto.getTodoDetail());
        }
    }

    private void addTodoToView(String title, String content) {
        // todoList에 이미 todo_id가 들어 있는 DTO가 있음
        for (TodoDTO dto : todoList) {
            if (dto.getTodoTitle().equals(title) && dto.getTodoDetail().equals(content)) {
                titleList.addElement(dto); // todo_id 포함된 객체 추가
                todoMap.put(title, content);
                break;
            }
        }
    }

    public void pushData(String title, String content) {
        // DB에 삽입
        TodoDAO.insertTodo(Session.getUserId(), title, content);

        // 최신 DB 리스트 다시 불러옴
        todoList = TodoDAO.todoList();

        // 뷰에 다시 반영
        addTodoToView(title, content);
    }

    public void deleteData(int todoId) {
        for (int i = 0; i < titleList.getSize(); i++) {
            TodoDTO dto = titleList.getElementAt(i);
            if (dto.getTodo_id() == todoId) {
                titleList.remove(i);
                break;
            }
        }
        todoList.removeIf(dto -> dto.getTodo_id() == todoId);
        todoMap.entrySet().removeIf(entry -> {
            return entry.getKey().equals(getTitleById(todoId));
        });
    }

    private String getTitleById(int todoId) {
        for (TodoDTO dto : todoList) {
            if (dto.getTodo_id() == todoId) {
                return dto.getTodoTitle();
            }
        }
        return null;
    }

    public Map<String, String> getTodoMap() {
        return todoMap;
    }

}
