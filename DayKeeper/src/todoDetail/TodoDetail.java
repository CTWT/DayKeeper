package todoDetail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
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
import todoList.TodoCardPanel;

public class TodoDetail extends JPanel {

    private List<TodoDTO> todoList;
    private final Map<String, String> todoMap = new LinkedHashMap<>();
    private JPanel cardContainer;

    public TodoDetail() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 타이틀
        add(CommonStyle.createTitleLabel(), BorderLayout.NORTH);

        // 카드 레이아웃
        cardContainer = new JPanel(new GridLayout(0, 2, 20, 20));
        cardContainer.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBorder(new EmptyBorder(10, 100, 10, 100));
        add(scrollPane, BorderLayout.CENTER);

        // 하단 버튼
        BottomPanelComponents bottom = CommonStyle.createBottomPanel();
        bottom.todoList.setVisible(true);
        bottom.todoDetailInput.setVisible(true);
        bottom.pillDetail.setVisible(true);
        bottom.statistics.setVisible(true);

        bottom.todoDetailInput.addActionListener(e -> {
            new TodoInput(this);
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

        // 데이터 로딩
        loadData();
    }

    public void loadData() {
        if (todoList != null) {
            todoList.clear();
        }
        todoList = TodoDAO.todoList();
        cardContainer.removeAll();
        todoMap.clear();

        for (TodoDTO dto : todoList) {
            addTodoToView(dto.getTodoTitle(), dto.getTodoDetail());
        }

        cardContainer.revalidate();
        cardContainer.repaint();
    }

    private void addTodoToView(String title, String content) {
        for (TodoDTO dto : todoList) {
            if (dto.getTodoTitle().equals(title) && dto.getTodoDetail().equals(content)) {
                TodoCardPanel card = new TodoCardPanel(dto, (id) -> {
                    int result = JOptionPane.showConfirmDialog(this, "삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        TodoDAO.deleteTodo(id);
                        deleteData(id);
                        loadData(); // 카드 재로딩
                    }
                });
                card.setPreferredSize(new Dimension(200, 100));
                cardContainer.add(card);

                todoMap.put(title, content);
                break;
            }
        }
    }

    public void pushData(String title, String content) {
        TodoDAO.insertTodo(Session.getUserId(), title, content);
        todoList = TodoDAO.todoList();
        addTodoToView(title, content);
        cardContainer.revalidate();
        cardContainer.repaint();
    }

    public void deleteData(int todoId) {
        todoList.removeIf(dto -> dto.getTodo_id() == todoId);
        todoMap.entrySet().removeIf(entry -> entry.getKey().equals(getTitleById(todoId)));
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
