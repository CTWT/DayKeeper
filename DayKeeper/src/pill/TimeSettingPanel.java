package pill;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : ChatGPT
 * 작성일 : 2025.05.16
 * 파일명 : TimeSettingPanel.java
 * 설명 : 원형 시계형 UI + 시간 설정, 뒤로가기 버튼 포함 패널
 */

public class TimeSettingPanel extends JPanel {
    private SupApp parent;

    public TimeSettingPanel(SupApp parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 타이틀
        JLabel title = new JLabel("복용 시간 시각화", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // 중앙 원형 시계판
        ClockPanel clockPanel = new ClockPanel();
        add(clockPanel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);

        JButton setBtn = new JButton("시간 설정");
        JButton backBtn = new JButton("뒤로");

        setBtn.setPreferredSize(new Dimension(100, 35));
        backBtn.setPreferredSize(new Dimension(100, 35));

        // 버튼 클릭 이벤트
        setBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "시간 설정 완료"));
        backBtn.addActionListener(e -> parent.showPanel("list"));

        bottomPanel.add(setBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * 중앙 원형 시계 모양 타임테이블 컴포넌트
     */
    class ClockPanel extends JPanel {
        public ClockPanel() {
            setPreferredSize(new Dimension(400, 400));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            int radius = 150;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(220, 220, 255));
            g2.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("맑은 고딕", Font.BOLD, 12));

            // 12개 시간 표시 (1시 ~ 12시)
            for (int i = 0; i < 12; i++) {
                double angle = Math.toRadians((i * 30) - 90); // 0도 기준 12시
                int tx = (int) (cx + Math.cos(angle) * (radius - 30));
                int ty = (int) (cy + Math.sin(angle) * (radius - 30));
                String label = (i == 0 ? "12시" : i + "시");
                int strWidth = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, tx - strWidth / 2, ty + 5);
            }
        }
    }
}
