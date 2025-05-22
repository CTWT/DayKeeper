package todoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import common.CommonStyle;
import config.BaseFrame;
import config.ScreenType;
import dbConnection.TodoDAO;
import dbConnection.TodoDTO;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.15
 * 파일명 : TodoList.java
 * 수정자 : 
 * 수정일 :
 * 설명 : TodoList화면
 */

public class TodoList extends JPanel {

    private JPanel cardContainer;
    private List<TodoDTO> todoList;

    public TodoList() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        // 상단 제목
        JLabel title = CommonStyle.createTitleLabel();
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        topPanel.add(title);

        // 날짜/시간
        JLabel dateTimeLabel = new JLabel(
                "접속시간 : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                SwingConstants.CENTER);
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateTimeLabel.setBorder(new EmptyBorder(5, 0, 10, 0));
        topPanel.add(dateTimeLabel);

        // 패널 전체를 상단에 추가
        add(topPanel, BorderLayout.NORTH);

        cardContainer = new JPanel(new GridLayout(0, 2, 20, 20)); // (rows=auto, cols=2, hgap, vgap)
        cardContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cardContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // 할 일 로딩 및 카드 추가
        loadAndRenderCards();

        // ✅ 화면이 다시 보여질 때마다 자동 새로고침
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refresh(); // 자동 갱신
            }
        });

        // 하단 공통 버튼
        CommonStyle.BottomPanelComponents bottom = CommonStyle.createBottomPanel();

        bottom.todoDetail.setVisible(true);
        bottom.pillDetail.setVisible(true);
        bottom.statistics.setVisible(true);

        bottom.todoDetail.addActionListener(e -> {
            BaseFrame f = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            f.showScreen(ScreenType.TODODETAIL);
        });
        bottom.pillDetail.addActionListener(e -> {
            BaseFrame f = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            f.showScreen(ScreenType.PILL);
        });
        bottom.statistics.addActionListener(e -> {
            BaseFrame f = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            f.showScreen(ScreenType.STATISTICS);
        });

        add(bottom.panel, BorderLayout.SOUTH);
    }

    // 카드 로딩 메서드
    private void loadAndRenderCards() {
        todoList = TodoDAO.todoList();
        cardContainer.removeAll();

        for (TodoDTO dto : todoList) {
            TodoCardPanel card = new TodoCardPanel(dto, this::handleComplete); // 2개 인자 생성자 호출
            card.setPreferredSize(new Dimension(200, 100)); // 카드 크기 고정 (선택)

            cardContainer.add(card);
        }

        cardContainer.revalidate();
        cardContainer.repaint();
    }

    // 완료 처리 후 새로고침
    private void handleComplete(int todoId) {
        try {
            TodoDAO.updateTodoYn(todoId); // void 메서드 호출
            loadAndRenderCards(); // 성공했다고 가정하고 화면 갱신
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "완료 처리 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void refresh() {
        loadAndRenderCards(); // 재 로딩
    }

    public static void main(String[] args) {
        BaseFrame b = new BaseFrame();
        TodoList t = new TodoList();
        b.setContentPane(t);
        b.setVisible(true);
    }
}