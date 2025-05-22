package statistics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.CommonStyle;
import dbConnection.PillDAO;
import dbConnection.StatisticsTodoDAO;

/**
 * 생성자 : 문원주
 * 생성일 : 25.05.19
 * 파일명 : TotalStatisticsPanel.java
 * 수정자 :
 * 수정일 :
 * 설명 : 시작하고나서 지금까지의 데이터 전부를 받아와서 %로 알려주는 프로그램
 */

public class TotalStatisticsPanel extends JPanel {
    private StatisticsTodoDAO todoDAO; // 투두리스트 데이터 접근 객체 선언
    private PillDAO pillDAO; // 영양제 데이터 접근 객체 선언

    public TotalStatisticsPanel(String userId, LocalDate date) {
        todoDAO = new StatisticsTodoDAO(); // 투두리스트 DAO 객체 생성
        pillDAO = new PillDAO(); // 영양제 DAO 객체 생성

        initComponents(userId, date); // UI 컴포넌트 초기화 메서드 호출
    }

    // 달성률(rate)에 따른 타이틀 텍스트(HTML) 반환 메서드
    private String getTitleTextFromRate(double rate) {
        if (rate >= 100)
            return "<span style='color:#FFD700; font-weight:bold; font-size:18pt;'>퍼펙트 케어러</span>";
        else if (rate >= 90)
            return "<span style='color:#00008B; font-weight:bold; font-size:17pt;'>완벽을 향해</span>";
        else if (rate >= 70)
            return "<span style='color:#8B0000; font-weight:bold; font-size:16pt;'>꾸준한 관리인</span>";
        else if (rate >= 50)
            return "<span style='color:#4B0082; font-weight:bold; font-size:16pt;'>타노스도 인정한 균형</span>";
        else if (rate >= 30)
            return "<span style='color:#808080; font-style:italic; font-size:15pt;'>관리가 소홀한 자</span>";
        else if (rate >= 10)
            return "<span style='color:#000000; font-style:italic; font-size:13pt;'>리스트 방치중</span>";
        else
            return "<span style='color:#666666; font-style:italic; font-size:13pt; text-decoration: line-through;'>의욕 실종</span>";
    }

    private void initComponents(String userId, LocalDate date) { // UI 컴포넌트 초기화 메서드

        // DAO를 통해 투두리스트 전체 달성률을 퍼센트로 반환받음
        double todoRateValue = todoDAO.getTotalTodo(userId, date);

        // DAO를 통해 영양제 복약률 전체 달성률을 퍼센트로 반환받음
        double pillRateValue = pillDAO.getTotalPill(userId, date);

        // 투두리스트 달성도 타이틀 JLabel 생성, HTML 텍스트 적용
        JLabel todoTitleLabel = new JLabel(
                String.format("<html>%s</html>", getTitleTextFromRate(todoRateValue)));
        todoTitleLabel.setFont(CommonStyle.TEXT_FONT);

        // 투두리스트 달성도 앞부분 텍스트 라벨 ("투두리스트 총 달성도: xx.xx% (")
        JLabel todoPrefixLabel = new JLabel(String.format("투두리스트 총 달성도: %.2f%% (", todoRateValue));
        todoPrefixLabel.setFont(CommonStyle.TEXT_FONT);

        // 투두리스트 달성도 닫는 괄호 라벨 ")"
        JLabel todoSuffixLabel = new JLabel(")");
        todoSuffixLabel.setFont(CommonStyle.TEXT_FONT);

        // 투두리스트 관련 라벨들을 한 줄에 붙이기 위한 패널 (FlowLayout, 좌측 정렬, 간격 0)
        JPanel todoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        todoPanel.add(todoPrefixLabel); // 앞부분 텍스트 추가
        todoPanel.add(todoTitleLabel); // 타이틀 텍스트 추가
        todoPanel.add(todoSuffixLabel); // 닫는 괄호 추가
        todoPanel.setOpaque(true); // 투명도 해제 (배경색 표시 가능하게)
        todoPanel.setBackground(Color.WHITE); // 배경색 하얀색으로 설정

        // 영양제 복약률 타이틀 JLabel 생성, HTML 텍스트 적용
        JLabel medTitleLabel = new JLabel(
                String.format("<html>%s</html>", getTitleTextFromRate(pillRateValue)));
        medTitleLabel.setFont(CommonStyle.TEXT_FONT);

        // 영양제 복약률 앞부분 텍스트 라벨 ("복약률 총 달성도: xx.xx% (")
        JLabel medPrefixLabel = new JLabel(String.format("복약률 총 달성도: %.2f%% (", pillRateValue));
        medPrefixLabel.setFont(CommonStyle.TEXT_FONT);

        // 영양제 복약률 닫는 괄호 라벨 ")"
        JLabel medSuffixLabel = new JLabel(")");
        medSuffixLabel.setFont(CommonStyle.TEXT_FONT);

        // 영양제 관련 라벨들을 한 줄에 붙이기 위한 패널 (FlowLayout, 좌측 정렬, 간격 0)
        JPanel medPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        medPanel.add(medPrefixLabel); // 앞부분 텍스트 추가
        medPanel.add(medTitleLabel); // 타이틀 텍스트 추가
        medPanel.add(medSuffixLabel); // 닫는 괄호 추가
        medPanel.setOpaque(true); // 투명도 해제
        medPanel.setBackground(Color.WHITE); // 배경색 하얀색으로 설정

        // 패널 레이아웃을 수직 방향 박스 레이아웃으로 설정
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 배경색을 공통 배경색으로 설정
        this.setBackground(CommonStyle.BACKGROUND_COLOR);

        // 패널 내부 여백을 위, 왼쪽, 아래, 오른쪽 10픽셀씩 설정해 컴포넌트 간 간격 확보
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 투두리스트 달성도 라벨을 패널에 추가
        this.add(todoPanel);

        // 라벨 사이에 5픽셀 높이의 빈 컴포넌트 추가하여 간격 조절
        this.add(Box.createRigidArea(new Dimension(0, 5)));

        // 복약률 달성도 라벨을 패널에 추가
        this.add(medPanel);
    }
}
