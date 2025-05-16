package todoList;

import javax.swing.border.EmptyBorder;

import common.CommonStyle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JPanel;

import config.BaseFrame;

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

        JLabel desc = new JLabel("<html>당일 첫 로그인 시 보여지는 화면<br>표시 - 당일 할일이 없습니다.</html>");
        desc.setFont(new Font("Arial", Font.PLAIN, 12));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(desc);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        String[] columns = { "할일제목", "상태" };
        Object[][] data = {
                { "할일제목", "완료" },
                { "할일제목", "완료" },
                { "할일제목", "완료" },
                { "할일제목", "완료" },
        };
        JTable table = new JTable(data, columns);
        table.setRowHeight(30);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        contentPanel.add(scrollPane);

        centerPanel.add(contentPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // 하단 버튼 구성 요소 받기
        CommonStyle.BottomPanelComponents bottom = CommonStyle.createBottomPanel();

        // 버튼 이벤트 지정
        bottom.todoDetail.addActionListener(e -> {
            System.out.println("오늘할일상세보기 클릭됨");
        });
        bottom.pillDetail.addActionListener(e -> {
            System.out.println("영양제 정보 클릭됨");
        });
        bottom.statistics.addActionListener(e -> {
            System.out.println("통계 클릭됨");
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
