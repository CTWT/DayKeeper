package statistics;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;

import common.CommonStyle;
import common.Session;

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
    private PhillDAO pillDAO; // 영양제 데이터 접근 객체 선언

    public TotalStatisticsPanel(String userId) {
        todoDAO = new StatisticsTodoDAO(); // 투두리스트 DAO 객체 생성
        pillDAO = new PhillDAO(); // 영양제 DAO 객체 생성

        initComponents(userId); // UI 컴포넌트 초기화 메서드 호출
    }

    private void initComponents(String userId) {

        // DAO를 통해 투두리스트 전체 달성률을 퍼센트로 반환받음
        double todoRateValue = todoDAO.getTotalTodo(userId);

        // DAO를 통해 영양제 복약률 전체 달성률을 퍼센트로 반환받음
        double pillRateValue = pillDAO.getTotalPill(userId);

        // 투두리스트 총 달성도를 표시하는 JLabel 생성, 정수형으로 변환해 텍스트에 포함
        JLabel todoRate = new JLabel("투두리스트 총 달성도: " + (int) todoRateValue + "%");

        // 영양제 복약률 총 달성도를 표시하는 JLabel 생성, 정수형으로 변환해 텍스트에 포함
        JLabel medRate = new JLabel("복약률 총 달성도: " + (int) pillRateValue + "%");

        // 라벨들에 공통 버튼 폰트 스타일 적용
        todoRate.setFont(CommonStyle.BUTTON_FONT);
        medRate.setFont(CommonStyle.BUTTON_FONT);

        // 패널 레이아웃을 수직 방향 박스 레이아웃으로 설정
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 배경색을 공통 배경색으로 설정
        this.setBackground(CommonStyle.BACKGROUND_COLOR);

        // 패널 내부 여백을 위, 왼쪽, 아래, 오른쪽 10픽셀씩 설정해 컴포넌트 간 간격 확보
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 투두리스트 달성도 라벨을 패널에 추가
        this.add(todoRate);

        // 라벨 사이에 5픽셀 높이의 빈 컴포넌트 추가하여 간격 조절
        this.add(Box.createRigidArea(new Dimension(0, 5)));

        // 복약률 달성도 라벨을 패널에 추가
        this.add(medRate);
    }
}
