package pill;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import common.CommonStyle;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정일 : 2025.05.16
 * 파일명 : TimeSettingPanel.java
 * 설명 : 원형 시계형 UI + 시간 선택 기능
 */

public class TimeSettingPanel extends JPanel {
    private PillApp parent;
    private int selectedHour = -1;

    public TimeSettingPanel(PillApp parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(CommonStyle.BACKGROUND_COLOR);

        // 상단 제목
        JLabel title = CommonStyle.createTitleLabel();
        title.setText("복용 시간 시각화");
        add(title, BorderLayout.NORTH);

        // 시계판
        ClockPanel clockPanel = new ClockPanel();
        add(clockPanel, BorderLayout.CENTER);

        // 하단 버튼
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        JButton setBtn = new JButton("시간 설정");
        JButton backBtn = new JButton("뒤로");

        setBtn.setPreferredSize(new Dimension(100, 35));
        backBtn.setPreferredSize(new Dimension(100, 35));

        CommonStyle.stylePrimaryButton(setBtn);
        backBtn.setFont(CommonStyle.TEXT_FONT);
        backBtn.setBackground(Color.LIGHT_GRAY);

        setBtn.addActionListener(e -> {
            if (selectedHour >= 0) {
                String msg = (selectedHour == 0 ? "12시" : selectedHour + "시") + "로 설정되었습니다.";
                JOptionPane.showMessageDialog(this, msg);
            } else {
                JOptionPane.showMessageDialog(this, "시간을 선택해주세요.");
            }
        });

        backBtn.addActionListener(e -> parent.showPanel("list"));

        bottomPanel.add(setBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * 시계판 내부 패널
     */
    class ClockPanel extends JPanel {
        private HashMap<Integer, Point> hourPoints = new HashMap<>();

        public ClockPanel() {
            setPreferredSize(new Dimension(400, 400));
            setBackground(CommonStyle.BACKGROUND_COLOR);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point click = e.getPoint();
                    for (int i = 0; i < 12; i++) {
                        Point p = hourPoints.get(i);
                        if (p != null && p.distance(click) < 20) {
                            selectedHour = i;
                            repaint();
                            break;
                        }
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            int radius = 140;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 배경 원
            g2.setColor(new Color(230, 240, 255));
            g2.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

            // 숫자
            g2.setFont(CommonStyle.BUTTON_FONT);
            hourPoints.clear();

            for (int i = 0; i < 12; i++) {
                double angle = Math.toRadians(i * 30 - 90);
                int tx = (int) (cx + Math.cos(angle) * (radius - 30));
                int ty = (int) (cy + Math.sin(angle) * (radius - 30));

                hourPoints.put(i, new Point(tx, ty));

                if (selectedHour == i) {
                    g2.setColor(new Color(100, 150, 255));
                    g2.fillOval(tx - 18, ty - 18, 36, 36);
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(Color.DARK_GRAY);
                }

                String label = (i == 0 ? "12시" : i + "시");
                FontMetrics fm = g2.getFontMetrics();
                int strWidth = fm.stringWidth(label);
                int strHeight = fm.getHeight();
                g2.drawString(label, tx - strWidth / 2, ty + strHeight / 4);
            }
        }
    }
}
