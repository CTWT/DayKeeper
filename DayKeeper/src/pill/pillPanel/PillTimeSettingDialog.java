// 기존 import 그대로 유지
package pill.pillPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.MultipleGradientPaint;
import java.util.HashMap;

import common.CommonStyle;
import dbConnection.PillAlramDAO;

public class PillTimeSettingDialog extends JDialog {
    private int selectedHour = -1;
    private boolean isPM = false;
    private JLabel selectedTimeLabel;
    private JButton setBtn;

    public PillTimeSettingDialog(Pill parent, JLabel timeInfoLabel) {
        setLayout(new BorderLayout());
        getContentPane().setBackground(CommonStyle.BACKGROUND_COLOR);
        setSize(500, 580);
        setLocationRelativeTo(null);

        PillAlramDAO alramDAO = new PillAlramDAO();
        alramDAO.settingNextInt();

        JLabel title = CommonStyle.createTitleLabel();
        title.setText("복용 시간 시각화");
        add(title, BorderLayout.NORTH);

        ClockPanel clockPanel = new ClockPanel();
        add(clockPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        setBtn = new JButton("시간 설정");
        JButton backBtn = new JButton("닫기");

        setBtn.setPreferredSize(new Dimension(135, 40));
        backBtn.setPreferredSize(new Dimension(135, 40));

        CommonStyle.stylePrimaryButton(setBtn);
        CommonStyle.styleExitButton(backBtn);

        setBtn.addActionListener(e -> {
            if (selectedHour >= 0) {
                int actualHour = isPM ? (selectedHour == 0 ? 12 : selectedHour + 12)
                        : (selectedHour == 0 ? 0 : selectedHour);
                alramDAO.registerAlarm(actualHour);

                String msg = String.format("%s %02d시로 설정되었습니다.", isPM ? "오후" : "오전",
                        selectedHour == 0 ? 12 : selectedHour);
                JOptionPane.showMessageDialog(PillTimeSettingDialog.this, msg, "알림", JOptionPane.INFORMATION_MESSAGE);
                selectedTimeLabel.setText(getNoticeString());
            } else {
                JOptionPane.showMessageDialog(PillTimeSettingDialog.this, "시간을 선택해주세요.", "경고",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            timeInfoLabel.setText(getCurrentTimeText());
            timeInfoLabel.repaint();
            dispose();
        });

        // ✅ 오전/오후 토글 버튼 개선
        JToggleButton ampmToggle = new JToggleButton("🌞 오전");
        ampmToggle.setFont(CommonStyle.BUTTON_FONT);
        ampmToggle.setPreferredSize(new Dimension(135, 40));
        CommonStyle.stylePrimaryButton(ampmToggle);

        ampmToggle.addActionListener(e -> {
            isPM = !isPM;
            ampmToggle.setText(isPM ? "🌙 오후" : "🌞 오전");
        });

        bottomPanel.add(ampmToggle);
        bottomPanel.add(setBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    class ClockPanel extends JPanel {
        private HashMap<Integer, Point> hourPoints = new HashMap<>();
        private Point mousePos = null;
        private double fixedAngle = Double.NaN;

        public ClockPanel() {
            setPreferredSize(new Dimension(400, 430));
            setLayout(null);
            setBackground(CommonStyle.BACKGROUND_COLOR);

            selectedTimeLabel = new JLabel(getNoticeString(), JLabel.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 255, 255), getWidth(), getHeight(),
                            new Color(220, 230, 255));
                    g2.setPaint(gradient);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                    String text = getText();
                    Font font = getFont();
                    g2.setFont(font);
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() + fm.getAscent()) / 2 - 4;
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.drawString(text, x + 2, y + 2);
                    g2.setColor(getForeground());
                    g2.drawString(text, x, y);
                    g2.dispose();
                }
            };

            selectedTimeLabel.setBounds(125, 10, 250, 50);
            selectedTimeLabel.setFont(CommonStyle.TITLE_FONT.deriveFont(Font.BOLD, 18f));
            selectedTimeLabel.setForeground(new Color(0, 51, 102));
            selectedTimeLabel.setBorder(BorderFactory.createLineBorder(CommonStyle.PRIMARY_COLOR, 2));
            selectedTimeLabel.setOpaque(false);
            selectedTimeLabel.setEnabled(false);
            add(selectedTimeLabel);

            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setBounds(50, 70, 380, 1);
            separator.setForeground(new Color(180, 180, 200));
            add(separator);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point click = e.getPoint();
                    for (int i = 0; i < 12; i++) {
                        Point p = hourPoints.get(i);
                        if (p != null && p.distance(click) < 20) {
                            selectedHour = i;
                            fixedAngle = Math.toRadians(i * 30 - 90);
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
            int cy = getHeight() / 2 + 40;
            int radius = 140;

            // ✅ 시계 원 그라데이션 배경
            GradientPaint clockGradient = new GradientPaint(cx - radius, cy - radius, new Color(245, 250, 255),
                    cx + radius, cy + radius, new Color(220, 230, 255));
            g2.setPaint(clockGradient);
            g2.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

            // ✅ 외곽 라디얼 그라데이션 효과
            Point2D center = new Point2D.Float(cx, cy);
            float[] dist = {0.95f, 1.0f};
            Color[] colors = {new Color(0, 0, 0, 0), new Color(30, 100, 180, 60)};
            RadialGradientPaint outerGlow = new RadialGradientPaint(center, radius + 5, dist, colors,
                    MultipleGradientPaint.CycleMethod.NO_CYCLE);
            g2.setPaint(outerGlow);
            g2.fillOval(cx - radius - 5, cy - radius - 5, (radius + 5) * 2, (radius + 5) * 2);

            hourPoints.clear();
            g2.setFont(CommonStyle.BUTTON_FONT.deriveFont(Font.BOLD, 15f));

            for (int i = 0; i < 12; i++) {
                double angle = Math.toRadians(i * 30 - 90);
                int tx = (int) (cx + Math.cos(angle) * (radius - 30));
                int ty = (int) (cy + Math.sin(angle) * (radius - 30));
                hourPoints.put(i, new Point(tx, ty));

                if (selectedHour == i) {
                    g2.setColor(CommonStyle.PRIMARY_COLOR);
                    g2.fillOval(tx - 18, ty - 18, 36, 36);
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(new Color(90, 90, 90));
                }

                g2.drawString((i == 0 ? "12시" : i + "시"), tx - 12, ty + 6);
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

    private String getNoticeString() {
        String hour = new PillAlramDAO().getRegisteredTime();
        if (hour.equals("--")) {
            return "-- : -- : --";
        }

        String ampm = "";
        int ihour = Integer.parseInt(hour);

        if (ihour > 12) {
            ihour -= 12;
            ampm = "오후";
        } else {
            ampm = "오전";
        }

        return String.format("⏰ 설정 시간 : %s %02d시", ampm, ihour);
    }

    public String getCurrentTimeText() {
        return getNoticeString();
    }
}
