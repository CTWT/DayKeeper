package common;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.15
 * 파일명 : CommonStyle.java
 * 수정자 : 임해균
 * 수정일 : 25.05.22
 * 설명 : Swing 공통 스타일 형식 지정 + 버튼 아이콘 상수 및 스타일 오버로딩
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

    // 공통 아이콘 이모지
    public static final String ICON_ADD = "➕";
    public static final String ICON_PILL = "💊";
    public static final String ICON_HOME = "🏠";
    public static final String ICON_TIME = "⏱";
    public static final String ICON_LOGIN = "🔐";
    public static final String ICON_SIGNUP = "✍";
    public static final String ICON_FIND = "🕵";
    public static final String ICON_DETAIL = "🔍";
    public static final String ICON_PILLINFO = "💊";
    public static final String ICON_STATISTICS = "📊";
    public static final String ICON_TODO = "📝";

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

    // 공통 버튼 스타일 (JButton)
    public static void stylePrimaryButton(JButton button) {
        applyButtonStyle(button, PRIMARY_COLOR);
    }

    // ✅ 공통 버튼 스타일 (JToggleButton) 오버로딩
    public static void stylePrimaryButton(JToggleButton button) {
        applyButtonStyle(button, PRIMARY_COLOR);
    }

    // 공통 닫기 버튼 스타일
    public static void styleExitButton(JButton button) {
        applyButtonStyle(button, EXIT_COLOR);
    }

    // 공통 삭제 버튼 스타일
    public static void styleDeleteButton(JButton button) {
        applyButtonStyle(button, DELETE_COLOR);
    }

    // 내부 버튼 스타일 공통 로직
    private static void applyButtonStyle(AbstractButton button, Color color) {
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

    public static void styleAddPillButton(JButton button) {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 초기 토글 상태 설정
        button.putClientProperty("toggled", false);

        // 클릭 동작은 ActionListener로 처리 (더 안정적)
        button.addActionListener(e -> {
            boolean toggled = (boolean) button.getClientProperty("toggled");
            button.putClientProperty("toggled", !toggled);
            button.repaint(); // UI 다시 그리기
        });

        // rollover 및 pressed 등 효과는 MouseListener로 유지
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.repaint();
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

                boolean toggled = (boolean) b.getClientProperty("toggled");

                Color baseColor = toggled ? PRIMARY_COLOR.darker().darker() : PRIMARY_COLOR;
                if (b.getModel().isRollover()) {
                    baseColor = toggled ? baseColor.brighter() : baseColor.darker();
                }

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

    // 공통 하단 버튼 구성
    public static BottomPanelComponents createBottomPanel() {
        BottomPanelComponents comp = new BottomPanelComponents();

        comp.todoDetailInput = new JButton(ICON_TODO + "할일 입력");
        comp.todoDetail = new JButton(ICON_DETAIL + "상세보기");
        comp.todoList = new JButton(ICON_HOME + "메인화면");
        comp.pillDetail = new JButton(ICON_PILLINFO + "영양제 정보");
        comp.statistics = new JButton(ICON_STATISTICS + "통계");
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
                comp.pillConsume
        );

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
