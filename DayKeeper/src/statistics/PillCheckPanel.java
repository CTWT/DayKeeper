package statistics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import common.CommonStyle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * 생성자 : 이주하
 * 생성일 : 25.05.19
 * 파일명 : PillCheckPanel.java
 * 수정자 :
 * 수정일 :
 * 설명 : 영양제 복용 여부에 따른 체크 그리드 생성
 */

public class PillCheckPanel extends JPanel {

    public PillCheckPanel(String userId, LocalDate date) {
        // 패널 레이아웃 2행 7열 그리드 (상단: 복약 상태, 하단: 요일 표시)
        setLayout(new GridLayout(2, 7));
        setPreferredSize(new Dimension(400, 48)); // 크기 지정
        setBackground(Color.WHITE); // 배경 흰색

        // 기준일로부터 이번 주 월요일 구하기
        LocalDate monday = date.with(DayOfWeek.MONDAY);

        // 기준 주간 복약 상태 배열 받아오기 (DAO 메서드는 userId, 기준주 월요일 인자를 받도록 수정 필요)
        Boolean[] status = new PhillDAO().getWeeklyMedicationStatus(userId, monday);

        LocalDate today = LocalDate.now();

        // 상단 행: 요일별 복약 상태 아이콘 표시
        for (int i = 0; i < 7; i++) {
            JPanel phillbox = new JPanel();
            phillbox.setPreferredSize(new Dimension(40, 40)); // 크기 고정
            phillbox.setOpaque(false); // 기본 투명

            JLabel symbolLabel = new JLabel();
            symbolLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
            symbolLabel.setFont(CommonStyle.BUTTON_FONT); // 폰트 굵고 크게 설정

            phillbox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2)); // 연회색 테두리
            LocalDate targetDate = monday.plusDays(i); // 해당 요일 날짜 계산

            // 복약 상태에 따른 배경색과 아이콘 표시
            if (status[i] == null) { // 데이터 없으면
                if (targetDate.isBefore(today)) { // 과거일 경우 미복약 표시
                    phillbox.setBackground(new Color(242, 242, 242)); // 연회색 배경
                    phillbox.setOpaque(true);
                    symbolLabel.setText("✗"); // 실패 아이콘
                    symbolLabel.setForeground(Color.DARK_GRAY);
                } else { // 미래일 경우 빈칸 표시
                    phillbox.setBackground(Color.WHITE); // 흰색 배경
                    phillbox.setOpaque(true);
                    symbolLabel.setText("-"); // 표시 없음
                    symbolLabel.setForeground(Color.GRAY);
                }
            } else if (status[i]) { // 복약 성공한 경우
                phillbox.setBackground(new Color(76, 154, 255)); // 파란 배경
                phillbox.setOpaque(true);
                symbolLabel.setText("✓"); // 성공 아이콘
                symbolLabel.setForeground(Color.WHITE);
            } else { // 미복약인 경우
                phillbox.setBackground(new Color(242, 242, 242)); // 연회색 배경
                phillbox.setOpaque(true);
                symbolLabel.setText("✗"); // 실패 아이콘
                symbolLabel.setForeground(Color.DARK_GRAY);
            }

            phillbox.setLayout(new java.awt.GridBagLayout()); // 아이콘 중앙 정렬
            phillbox.add(symbolLabel); // 아이콘 추가
            add(phillbox); // 패널에 박스 추가
        }

        // 하단 행: 요일명 라벨 표시
        String[] dayNames = { "월", "화", "수", "목", "금", "토", "일" };
        for (int i = 0; i < 7; i++) {
            JLabel dayLabel = new JLabel(dayNames[i]);
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
            dayLabel.setFont(CommonStyle.TEXT_FONT); // 기본 폰트 크기
            add(dayLabel); // 패널에 요일 라벨 추가
        }
    }
}
