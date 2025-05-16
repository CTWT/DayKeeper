package common;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.15
 * 파일명 : CommenStyle.java
 * 수정자 : 
 * 수정일 :
 * 설명 : swing 공통 스타일 형식 지정
 */
public class CommonStyle {

    // 공통 색상
    public static final Color PRIMARY_COLOR = new Color(30, 100, 180);
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final Color ERROR_COLOR = Color.RED;
    public static final Color LINK_COLOR = Color.BLUE.darker();

    // 공통 폰트
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 40);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 14);
    public static final Font TEXT_FONT = new Font("SansSerif", Font.PLAIN, 13);

    // 하단 버튼 정보를 담기 위한 내부 클래스
    public static class BottomPanelComponents {
        public JPanel panel;
        public JButton todoDetail;
        public JButton pillDetail;
        public JButton statistics;
        public JButton returnPage;
    }

    // 공통 버튼 스타일
    public static void stylePrimaryButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 10, true)); // 둥근 테두리
        button.setOpaque(true);
    }

    // 공통 텍스트필드 밑줄 스타일
    public static void underline(JTextField field) {
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    }

    // 공통 타이틀 라벨
    public static JLabel createTitleLabel() {
        JLabel label = new JLabel("DAY-KEEPER", SwingConstants.CENTER);
        label.setFont(TITLE_FONT);
        label.setForeground(PRIMARY_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 상단 여백 추가 공통
        return label;
    }

    // 공통 라벨
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TEXT_FONT);
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    public static BottomPanelComponents createBottomPanel() {
        BottomPanelComponents comp = new BottomPanelComponents();

        comp.todoDetail = new JButton("오늘할일상세보기");
        comp.pillDetail = new JButton("영양제 정보");
        comp.statistics = new JButton("통계");
        comp.returnPage = new JButton("돌아가기");

        stylePrimaryButton(comp.todoDetail);
        stylePrimaryButton(comp.pillDetail);
        stylePrimaryButton(comp.statistics);
        stylePrimaryButton(comp.returnPage);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.add(comp.todoDetail);
        bottomPanel.add(comp.pillDetail);
        bottomPanel.add(comp.statistics);
        bottomPanel.add(comp.returnPage);

        comp.panel = bottomPanel;
        return comp;
    }
}
