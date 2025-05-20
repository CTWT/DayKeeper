package pill.pillPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import common.CommonStyle;
import pill.pillDAO.PillAlramDAO;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 2025.05.16
 * 수정자 : 김관호
 * 수정일 : 2025.05.20
 * 파일명 : PillTimeSettingDialog.java
 * 설명 : 복용 시간 시각화 다이얼로그 (시계형 UI + 설정시간 라벨)
 */

public class PillTimeSettingDialog extends JDialog {
    private int selectedHour = -1;

    public PillTimeSettingDialog(Pill parent) {
        setLayout(new BorderLayout());
        setBackground(CommonStyle.BACKGROUND_COLOR);
        setSize(500, 500);
        setLocationRelativeTo(null);

        PillAlramDAO alramDAO = new PillAlramDAO();
        alramDAO.settingNextInt();

        JLabel title = CommonStyle.createTitleLabel();
        title.setText("복용 시간 시각화");
        add(title, BorderLayout.NORTH);

        ClockPanel clockPanel = new ClockPanel();
        add(clockPanel, BorderLayout.CENTER);

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
                alramDAO.registerAlarm(selectedHour);
                String msg = (selectedHour == 0 ? "12시" : selectedHour + "시") + "로 설정되었습니다.";
                JOptionPane.showMessageDialog(PillTimeSettingDialog.this, msg, "알림", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(PillTimeSettingDialog.this, "시간을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> dispose());

        bottomPanel.add(setBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    class ClockPanel extends JPanel {
        private HashMap<Integer, Point> hourPoints = new HashMap<>();
        private Point mousePos = null;
        private double fixedAngle = Double.NaN;
        private JLabel selectedTimeLabel;

        public ClockPanel() {
            setPreferredSize(new Dimension(400, 400));
            setLayout(null);
            setBackground(CommonStyle.BACKGROUND_COLOR);

            // 설정 시간 라벨 - 흰 배경 + 파란 글씨 + 테두리
            selectedTimeLabel = new JLabel("설정 시간 : --시", JLabel.CENTER);
            selectedTimeLabel.setBounds(140, 10, 220, 35);
            selectedTimeLabel.setFont(CommonStyle.BUTTON_FONT.deriveFont(Font.BOLD, 15f));
            selectedTimeLabel.setOpaque(true);
            selectedTimeLabel.setBackground(Color.WHITE);
            selectedTimeLabel.setForeground(CommonStyle.PRIMARY_COLOR);
            selectedTimeLabel.setBorder(BorderFactory.createLineBorder(CommonStyle.PRIMARY_COLOR, 2));
            selectedTimeLabel.setEnabled(false);
            add(selectedTimeLabel);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point click = e.getPoint();
                    for (int i = 0; i < 12; i++) {
                        Point p = hourPoints.get(i);
                        if (p != null && p.distance(click) < 20) {
                            selectedHour = i;
                            double angle = Math.toRadians(i * 30 - 90);
                            fixedAngle = angle;
                            selectedTimeLabel.setText("설정 시간 : " + (i == 0 ? "12시" : i + "시"));
                            repaint();
                            break;
                        }
                    }
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    if (selectedHour == -1) {
                        mousePos = e.getPoint();
                        repaint();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cx = getWidth() / 2;
            int cy = getHeight() / 2 + 20; // 시계 약간 아래로 내려서 라벨과 겹치지 않게
            int radius = 140;

            g2.setColor(new Color(230, 240, 255));
            g2.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

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
                g2.drawString(label, tx - fm.stringWidth(label) / 2, ty + fm.getHeight() / 4);
            }

            if (!Double.isNaN(fixedAngle)) {
                drawHand(g2, cx, cy, fixedAngle, radius);
            } else if (mousePos != null) {
                double angle = Math.atan2(mousePos.y - cy, mousePos.x - cx);
                drawHand(g2, cx, cy, angle, radius);
            }

            g2.setColor(Color.GRAY);
            g2.fillOval(cx - 4, cy - 4, 8, 8);
        }

        private void drawHand(Graphics2D g2, int cx, int cy, double angle, int radius) {
            int hx = (int) (cx + Math.cos(angle) * (radius - 60));
            int hy = (int) (cy + Math.sin(angle) * (radius - 60));
            g2.setColor(new Color(100, 100, 220));
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(cx, cy, hx, hy);
        }
    }
}
