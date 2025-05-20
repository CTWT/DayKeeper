package todoDetail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;
import common.Session;
import config.BaseFrame;
import config.ScreenType;
import todoList.TodoDAO;
import todoList.TodoDTO;

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

    public TodoDetail() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 타이틀
        JLabel title = CommonStyle.createTitleLabel();
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        // 중앙 영역 패널
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(10, 100, 10, 100));

        add(centerPanel, BorderLayout.CENTER);

        // 하단 버튼 구성
        BottomPanelComponents bottom = CommonStyle.createBottomPanel();

        bottom.todoDetailInput.addActionListener(e -> {
            JDialog d = new Input();
        });
        bottom.todoDetail.setVisible(false); // 현재 화면
        bottom.pillDetail.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.PILL);
        });
        bottom.statistics.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.STATISTICS);
        });
        bottom.returnPage.setVisible(false);

        add(bottom.panel, BorderLayout.SOUTH);
    }

    // main 테스트용
    public static void main(String[] args) {
        BaseFrame f = new BaseFrame();
        f.setContentPane(new TodoDetail());
        f.setVisible(true);
    }
}
