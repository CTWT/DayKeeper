package todoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import common.CommonStyle;
import common.Session;
import config.BaseFrame;
import config.ScreenType;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.15
 * 파일명 : TodoList.java
 * 수정자 : 
 * 수정일 :
 * 설명 : TodoList화면
 */

public class TodoList extends JPanel {

    public TodoList() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel title = CommonStyle.createTitleLabel();
        title.setBorder(new EmptyBorder(20, 0, 0, 0)); // 상단 여백 추가
        add(title, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(10, 100, 10, 100));

        JLabel subTitle = new JLabel("todo-list");
        subTitle.setFont(new Font("Arial", Font.BOLD, 16));
        centerPanel.add(subTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // contentPanel 생성 후
        List<String[]> todoData = TodoDAO.todoList(Session.getUserId()); // ← 로그인 ID로 교체

        String[] columnNames = { "할 일", "상태" };
        String[][] rowData;

        if (todoData.isEmpty()) {
            rowData = new String[][] { { "당일 할 일이 없습니다.", "" } };
        } else {
            rowData = todoData.toArray(new String[0][0]);
        }

        // JTable 생성 : rowData 는 내용 columnNames는 헤더
        JTable table = new JTable(rowData, columnNames);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 13));

        // JTable을 JScroll에 담P기
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        centerPanel.add(scrollPane);
        add(centerPanel, BorderLayout.CENTER);

        // 하단 버튼 구성 요소 받기
        CommonStyle.BottomPanelComponents bottom = CommonStyle.createBottomPanel();

        // 버튼 이벤트 지정
        bottom.todoDetail.addActionListener(e -> {
            System.out.println("오늘할일상세보기 클릭됨");
        });
        bottom.pillDetail.addActionListener(e -> {
            BaseFrame pillFrame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            pillFrame.showScreen(ScreenType.PILL);
        });
        bottom.statistics.addActionListener(e -> {
            BaseFrame statFrame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            statFrame.showScreen(ScreenType.STATISTICS);
        });

        // '돌아가기' 버튼은 이 화면에서 숨김
        bottom.returnPage.setVisible(false);

        // 화면에 추가
        add(bottom.panel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        BaseFrame b = new BaseFrame();
        TodoList t = new TodoList();
        b.setContentPane(t);
        b.setVisible(true);
    }
}
