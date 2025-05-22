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
    public static final Color EXIT_COLOR = Color.GRAY;
    public static final Color DELETE_COLOR = new Color(200, 54, 54);

    // 공통 폰트
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 40);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font TEXT_FONT = new Font("SansSerif", Font.BOLD, 16);

    // 하단 버튼 정보를 담기 위한 내부 클래스
    public static class BottomPanelComponents {
        public JPanel panel;
        public JButton todoDetailInput;
        public JButton todoDetail;
        public JButton todoList;
        // pill영역 요구 버튼
        public JButton pillAdd;
        public JButton pillTimeSetting;
        public JButton pillConsume;
        public JButton returnHome;
        public JButton pillDetail;
        public JButton statistics;
        // public JButton returnPage;
    }

    // 공통 버튼 스타일
    public static void stylePrimaryButton(JButton button) {
        button.setContentAreaFilled(false); // 기본 배경 비활성화 (커스터마이징 위함)
        button.setFocusPainted(false); // 포커스 테두리 제거
        button.setBorderPainted(false); // 기본 테두리 제거
        button.setForeground(Color.WHITE); // 텍스트 색상
        button.setFont(BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 마우스 리스너 추가 (hover, pressed 효과)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker()); // hover 시 색상 진하게
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR); // 원래 색상
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker().darker()); // 눌림 색상
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker()); // 눌렀다 뗄 때 hover 상태
            }
        });

        // 둥근 테두리 & 배경 효과 커스터마이징을 위한 UI 덮어쓰기
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = b.getWidth();
                int height = b.getHeight();

                // 버튼 색상
                Color baseColor = b.getModel().isPressed() ? PRIMARY_COLOR.darker().darker()
                        : b.getModel().isRollover() ? PRIMARY_COLOR.darker() : PRIMARY_COLOR;

                // 그라데이션 효과
                GradientPaint gp = new GradientPaint(0, 0, baseColor.brighter(), 0, height, baseColor);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, width, height, 20, 20); // 둥근 사각형

                // 텍스트 출력
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

    // 공통 닫기 버튼 스타일
    public static void styleExitButton(JButton button) {
        button.setContentAreaFilled(false); // 기본 배경 비활성화 (커스터마이징 위함)
        button.setFocusPainted(false); // 포커스 테두리 제거
        button.setBorderPainted(false); // 기본 테두리 제거
        button.setForeground(Color.WHITE); // 텍스트 색상
        button.setFont(BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 마우스 리스너 추가 (hover, pressed 효과)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(EXIT_COLOR.darker()); // hover 시 색상 진하게
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(EXIT_COLOR); // 원래 색상
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(EXIT_COLOR.darker().darker()); // 눌림 색상
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(EXIT_COLOR.darker()); // 눌렀다 뗄 때 hover 상태
            }
        });

        // 둥근 테두리 & 배경 효과 커스터마이징을 위한 UI 덮어쓰기
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = b.getWidth();
                int height = b.getHeight();

                // 버튼 색상
                Color baseColor = b.getModel().isPressed() ? EXIT_COLOR.darker().darker()
                        : b.getModel().isRollover() ? EXIT_COLOR.darker() : EXIT_COLOR;

                // 그라데이션 효과
                GradientPaint gp = new GradientPaint(0, 0, baseColor.brighter(), 0, height, baseColor);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, width, height, 20, 20); // 둥근 사각형

                // 텍스트 출력
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

    // 공통 버튼 스타일
    public static void styleDeleteButton(JButton button) {
        button.setContentAreaFilled(false); // 기본 배경 비활성화 (커스터마이징 위함)
        button.setFocusPainted(false); // 포커스 테두리 제거
        button.setBorderPainted(false); // 기본 테두리 제거
        button.setForeground(Color.WHITE); // 텍스트 색상
        button.setFont(BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 마우스 리스너 추가 (hover, pressed 효과)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(DELETE_COLOR.darker()); // hover 시 색상 진하게
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(DELETE_COLOR); // 원래 색상
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(DELETE_COLOR.darker().darker()); // 눌림 색상
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(DELETE_COLOR.darker()); // 눌렀다 뗄 때 hover 상태
            }
        });

        // 둥근 테두리 & 배경 효과 커스터마이징을 위한 UI 덮어쓰기
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = b.getWidth();
                int height = b.getHeight();

                // 버튼 색상
                Color baseColor = b.getModel().isPressed() ? DELETE_COLOR.darker().darker()
                        : b.getModel().isRollover() ? DELETE_COLOR.darker() : DELETE_COLOR;

                // 그라데이션 효과
                GradientPaint gp = new GradientPaint(0, 0, baseColor.brighter(), 0, height, baseColor);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, width, height, 20, 20); // 둥근 사각형

                // 텍스트 출력
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

        comp.todoDetailInput = new JButton("오늘 할일 입력");
        comp.todoDetail = new JButton("상세보기");
        comp.todoList = new JButton("메인화면");
        comp.pillDetail = new JButton("영양제 정보");
        comp.statistics = new JButton("통계");
        comp.pillAdd = new JButton("➕ 추가");
        comp.returnHome = new JButton("🏠 처음으로");
        comp.pillTimeSetting = new JButton("⏱ 시간 설정");
        comp.pillConsume = new JButton("💊 영양제 섭취");

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
        }

        stylePrimaryButton(comp.todoDetailInput);
        stylePrimaryButton(comp.todoDetail);
        stylePrimaryButton(comp.todoList);
        stylePrimaryButton(comp.statistics);
        stylePrimaryButton(comp.pillAdd);
        stylePrimaryButton(comp.pillConsume);
        stylePrimaryButton(comp.pillDetail);
        stylePrimaryButton(comp.returnHome);
        stylePrimaryButton(comp.pillTimeSetting);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.add(comp.returnHome); // 1. 홈
        bottomPanel.add(comp.todoDetailInput); // 2. 투두 추가
        bottomPanel.add(comp.todoDetail); // 3. 투두 상세
        bottomPanel.add(comp.pillAdd); // 4. 영양제 추가
        bottomPanel.add(comp.pillDetail); // 5. 영양제 상세
        bottomPanel.add(comp.pillTimeSetting); // 6. 시간 설정
        bottomPanel.add(comp.pillConsume); // 7. 복용
        bottomPanel.add(comp.statistics); // 8. 통계

        comp.panel = bottomPanel;
        return comp;
    }
}
