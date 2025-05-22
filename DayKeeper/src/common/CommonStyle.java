package common;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.15
 * 파일명 : CommonStyle.java
 * 수정자 : 임해균
 * 수정일 : 25.05.22
 * 설명 : swing 공통 스타일 형식 지정 + 버튼 아이콘 상수 적용
 */
public class CommonStyle {

    // 공통 색상
    public static final Color PRIMARY_COLOR = new Color(30, 100, 180);
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final Color ERROR_COLOR = Color.RED;
    public static final Color LINK_COLOR = Color.BLUE.darker();
    public static final Color EXIT_COLOR = Color.GRAY;
    public static final Color DELETE_COLOR = new Color(200, 54, 54);

    // 공통 폰트
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 40);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font TEXT_FONT = new Font("SansSerif", Font.BOLD, 16);

    // ✅ 공통 이모지 아이콘 상수
    public static final String ICON_ADD = "➕";
    public static final String ICON_PILL = "💊";
    public static final String ICON_HOME = "🏠";
    public static final String ICON_TIME = "⏱";

    // 하단 버튼 정보를 담기 위한 내부 클래스
    public static class BottomPanelComponents {
        public JPanel panel;
        public JButton todoDetailInput;
        public JButton todoDetail;
        public JButton todoList;
        public JButton pillAdd;
        public JButton pillTimeSetting;
        public JButton pillConsume;
        public JButton returnHome;
        public JButton pillDetail;
        public JButton statistics;
    }

    // 공통 버튼 스타일
    public static void stylePrimaryButton(JButton button) {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker().darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker());
            }
        });

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = b.getWidth();
                int height = b.getHeight();

                Color baseColor = b.getModel().isPressed() ? PRIMARY_COLOR.darker().darker()
                        : b.getModel().isRollover() ? PRIMARY_COLOR.darker() : PRIMARY_COLOR;

                GradientPaint gp = new GradientPaint(0, 0, baseColor.brighter(), 0, height, baseColor);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, width, height, 20, 20);

                FontMetrics fm = g2.getFontMetrics();
                Rectangle stringBounds = fm.getStringBounds(b.getText(), g2).getBounds();
                int textX = (width - stringBounds.width) / 2;
                int textY = (height - stringBounds.height) / 2 + fm.getAscent();

                g2.setColor(b.getForeground());
                g2.setFont(b.getFont());
                g2.drawString(b.getText(), textX, textY);

                g2.dispose();
            }
        });

        button.setPreferredSize(new Dimension(135, 40));
    }

    public static void styleExitButton(JButton button) {
        applyColorButtonStyle(button, EXIT_COLOR);
    }

    public static void styleDeleteButton(JButton button) {
        applyColorButtonStyle(button, DELETE_COLOR);
    }

    private static void applyColorButtonStyle(JButton button, Color color) {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(color.darker().darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(color.darker());
            }
        });

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = b.getWidth();
                int height = b.getHeight();

                Color baseColor = b.getModel().isPressed() ? color.darker().darker()
                        : b.getModel().isRollover() ? color.darker() : color;

                GradientPaint gp = new GradientPaint(0, 0, baseColor.brighter(), 0, height, baseColor);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, width, height, 20, 20);

                FontMetrics fm = g2.getFontMetrics();
                Rectangle stringBounds = fm.getStringBounds(b.getText(), g2).getBounds();
                int textX = (width - stringBounds.width) / 2;
                int textY = (height - stringBounds.height) / 2 + fm.getAscent();

                g2.setColor(b.getForeground());
                g2.setFont(b.getFont());
                g2.drawString(b.getText(), textX, textY);

                g2.dispose();
            }
        });

        button.setPreferredSize(new Dimension(135, 40));
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
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        return label;
    }

    // 공통 라벨
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TEXT_FONT);
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    // 공통 하단 버튼 패널
    public static BottomPanelComponents createBottomPanel() {
        BottomPanelComponents comp = new BottomPanelComponents();

        comp.todoDetailInput = new JButton("오늘 할일 입력");
        comp.todoDetail = new JButton("상세보기");
        comp.todoList = new JButton("메인화면");
        comp.pillDetail = new JButton("영양제 정보");
        comp.statistics = new JButton("통계");

        // ✅ 이모지 상수 적용
        comp.pillAdd = new JButton(ICON_ADD + " 추가");
        comp.returnHome = new JButton(ICON_HOME + " 처음으로");
        comp.pillTimeSetting = new JButton(ICON_TIME + " 시간 설정");
        comp.pillConsume = new JButton(ICON_PILL + " 영양제 섭취");

        List<JButton> buttons = Arrays.asList(
                comp.todoDetailInput,
                comp.todoDetail,
                comp.todoList,
                comp.pillDetail,
                comp.statistics,
                comp.pillAdd,
                comp.returnHome,
                comp.pillTimeSetting,
                comp.pillConsume);

        for (JButton btn : buttons) {
            btn.setVisible(false);
            stylePrimaryButton(btn);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.add(comp.returnHome);
        bottomPanel.add(comp.todoDetailInput);
        bottomPanel.add(comp.todoDetail);
        bottomPanel.add(comp.pillAdd);
        bottomPanel.add(comp.pillDetail);
        bottomPanel.add(comp.pillTimeSetting);
        bottomPanel.add(comp.pillConsume);
        bottomPanel.add(comp.statistics);

        comp.panel = bottomPanel;
        return comp;
    }
}
