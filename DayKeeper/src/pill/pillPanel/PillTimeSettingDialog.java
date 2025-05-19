package pill.pillPanel;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;

import common.CommonStyle;
import pill.pillDAO.PillAlramDAO;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 2025.05.16
 * 수정자 : 김관호
 * 수정일 : 2025.05.19
 * 파일명 : TimeSettingPanel.java
 * 설명 : 원형 시계형 UI + 마우스 방향 따라 움직이는 시계침 구현 + 선택 시 고정
 */

public class PillTimeSettingDialog extends JDialog {
    private int selectedHour = -1;  // 선택된 시간 초기값

    /**
     * TimeSettingPanel 생성자
     * - 부모 프레임을 인자로 받아 UI를 구성하고 이벤트를 설정
     * 
     * @param parent 부모 프레임
     */
    public PillTimeSettingDialog(Pill parent) {
        setLayout(new BorderLayout());
        setBackground(CommonStyle.BACKGROUND_COLOR);
        setSize(500, 500);
        
        // 알람 설정 DAO 객체 생성 및 초기화
        PillAlramDAO alramDAO = new PillAlramDAO();
        alramDAO.settingNextInt();

        // 타이틀 라벨 추가
        JLabel title = CommonStyle.createTitleLabel();
        title.setText("복용 시간 시각화");
        add(title, BorderLayout.NORTH);

        // 시계 패널 추가
        ClockPanel clockPanel = new ClockPanel();
        add(clockPanel, BorderLayout.CENTER);

        // 버튼 패널 생성
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        // 버튼 생성 및 설정
        JButton setBtn = new JButton("시간 설정");
        JButton backBtn = new JButton("뒤로");

        setBtn.setPreferredSize(new Dimension(100, 35));
        backBtn.setPreferredSize(new Dimension(100, 35));

        CommonStyle.stylePrimaryButton(setBtn);
        backBtn.setFont(CommonStyle.TEXT_FONT);
        backBtn.setBackground(Color.LIGHT_GRAY);

        // 시간 설정 버튼 클릭 이벤트
        setBtn.addActionListener(e -> {
            if (selectedHour >= 0) {
                alramDAO.registerAlarm(selectedHour);

                //String msg = (selectedHour == 0 ? "12시" : selectedHour + "시") + "로 설정되었습니다.";
                //JOptionPane.showMessageDialog(this, msg);
            } else {
                //JOptionPane.showMessageDialog(this, "시간을 선택해주세요.");
            }
        });

        // 뒤로 버튼 클릭 이벤트
        backBtn.addActionListener(e -> dispose());

        // 버튼을 하단 패널에 추가
        bottomPanel.add(setBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * ClockPanel 클래스
     * - 원형 시계 형태로 시간을 시각화하여 선택할 수 있는 패널
     */
    class ClockPanel extends JPanel {
        private HashMap<Integer, Point> hourPoints = new HashMap<>();  // 시각별 위치 저장
        private Point mousePos = null;  // 마우스 위치
        private double fixedAngle = Double.NaN;  // 고정된 시침 각도

        /**
         * ClockPanel 생성자
         * - 시계 UI 설정 및 마우스 이벤트 처리기 등록
         */
        public ClockPanel() {
            setPreferredSize(new Dimension(400, 400));
            setBackground(CommonStyle.BACKGROUND_COLOR);

            // 마우스 클릭 이벤트 처리
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
                            repaint();
                            break;
                        }
                    }
                }
            });

            // 마우스 이동 이벤트 처리
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

        /**
         * 시계 UI를 그리는 메서드
         * 
         * @param g 그래픽 객체
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            int radius = 140;

            // 시계 배경 원 그리기
            g2.setColor(new Color(230, 240, 255));
            g2.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

            // 시계 숫자 배치
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

            // 시침 그리기
            if (!Double.isNaN(fixedAngle)) {
                drawHand(g2, cx, cy, fixedAngle, radius);
            } else if (mousePos != null) {
                double angle = Math.atan2(mousePos.y - cy, mousePos.x - cx);
                drawHand(g2, cx, cy, angle, radius);
            }

            // 중심 원
            g2.setColor(Color.GRAY);
            g2.fillOval(cx - 4, cy - 4, 8, 8);
        }

        /**
         * 시계침 그리기
         */
        private void drawHand(Graphics2D g2, int cx, int cy, double angle, int radius) {
            int hx = (int) (cx + Math.cos(angle) * (radius - 60));
            int hy = (int) (cy + Math.sin(angle) * (radius - 60));
            g2.setColor(new Color(100, 100, 220));
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(cx, cy, hx, hy);
        }
    }
}
