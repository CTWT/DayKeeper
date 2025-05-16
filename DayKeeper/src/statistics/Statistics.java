package statistics;

import common.CommonStyle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.BasicStroke;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

/*
 * 생성자 : 문원주
 * 생성일 : 25.05.16
 * 파일명 : Statistics.java
 * 수정자 : 
 * 수정일 : 
 * 설명 : 통계 페이지의 틀 구현 test용도
 */

public class Statistics extends JPanel {

    // 차트 내 폰트 기본 설정 (SansSerif, 일반, 14pt)
    public static final Font CHART_FONT = new Font("SansSerif", Font.PLAIN, 14);

    public Statistics() {
        setLayout(null); // 절대 위치 기반 레이아웃 사용
        setBackground(CommonStyle.BACKGROUND_COLOR); // 배경색 설정 (공통 스타일에서 가져옴)

        // 상단 타이틀 라벨 생성 및 위치 지정
        JLabel titleLabel = CommonStyle.createTitleLabel();
        titleLabel.setBounds(250, 10, 300, 35);
        add(titleLabel); // 패널에 추가

        // 차트 데이터셋 생성 (테스트용 하드코딩된 값, 추후 DB 연동 시 교체 예정)
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(80, "달성률", "월"); // 월요일 달성률 80%
        dataset.addValue(60, "달성률", "화"); // 화요일 달성률 60%
        dataset.addValue(20, "달성률", "수"); // 수요일 달성률 20%
        dataset.addValue(70, "달성률", "목"); // 목요일 달성률 70%
        dataset.addValue(50, "달성률", "금"); // 금요일 달성률 50%
        dataset.addValue(40, "달성률", "토"); // 토요일 달성률 40%
        dataset.addValue(85, "달성률", "일"); // 일요일 달성률 85%

        // 선 그래프 생성
        JFreeChart lineChart = ChartFactory.createLineChart(
                "주간 투두리스트 달성도", // 차트 제목
                "", // x축 라벨 (빈 문자열)
                "달성률(%)", // y축 라벨
                dataset, // 데이터셋
                PlotOrientation.VERTICAL, // 세로 방향 차트
                false, // 범례 비표시
                true, // 툴팁 표시
                false // URL 링크 없음
        );

        // 차트 제목 폰트 설정
        lineChart.getTitle().setFont(CHART_FONT);
        // 차트 배경색 설정 (공통 스타일 배경색)
        lineChart.setBackgroundPaint(CommonStyle.BACKGROUND_COLOR);

        // 차트의 플롯(차트 내 영역) 가져오기
        CategoryPlot plot = lineChart.getCategoryPlot();

        // x축 및 y축 폰트 설정
        plot.getDomainAxis().setLabelFont(CHART_FONT);
        plot.getRangeAxis().setLabelFont(CHART_FONT);
        plot.getDomainAxis().setTickLabelFont(CHART_FONT);
        plot.getRangeAxis().setTickLabelFont(CHART_FONT);

        // y축 범위 설정 (0% ~ 100%)
        plot.getRangeAxis().setRange(0, 100);

        // 플롯 배경색 설정 (공통 스타일 배경색)
        plot.setBackgroundPaint(CommonStyle.BACKGROUND_COLOR);

        // 렌더러 생성 (그래프 선과 점 그리는 역할)
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3.5f)); // 첫 번째 시리즈(달성률) 선 굵기 3.5로 지정
        renderer.setSeriesShapesVisible(0, false); // 점 없이 선만 보이도록 설정

        // 플롯에 커스텀 렌더러 적용
        plot.setRenderer(renderer);

        // 차트 패널 생성, 크기 및 위치 설정
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setBounds(80, 55, 600, 300);
        add(chartPanel); // 패널에 추가

        // 복약 여부 표시용 패널 생성 (2행 7열 그리드 레이아웃) - 이후 변경 예정
        JPanel medicationPanel = new JPanel(new GridLayout(2, 7));
        medicationPanel.setBounds(225, 375, 350, 65);
        // 7일간 복약 여부를 색깔로 표시 (예시 데이터)
        for (int i = 0; i < 7; i++) {
            JLabel medStatus = new JLabel();
            medStatus.setOpaque(true); // 배경색 적용 가능하게 설정
            if (i % 2 == 0) {
                medStatus.setBackground(new Color(100, 255, 100)); // 초록: 복약함 표시 예시
            } else {
                medStatus.setBackground(new Color(255, 64, 64)); // 빨강: 미복약 표시 예시
            }
            if (i == 5 || i == 6) {
                medStatus.setBackground(Color.WHITE); // 토요일, 일요일은 흰색으로 구분
            }
            medStatus.setBorder(new LineBorder(Color.BLACK)); // 테두리 검정색 선
            medicationPanel.add(medStatus); // 패널에 추가
        }

        // 각 요일별 라벨 추가 (아래 행)
        String[] days = { "월", "화", "수", "목", "금", "토", "일" };
        for (String day : days) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(CommonStyle.TEXT_FONT); // 공통 스타일 폰트 사용
            medicationPanel.add(dayLabel); // 패널에 추가
            medicationPanel.setBackground(Color.WHITE); // 배경은 흰색으로 설정
        }
        add(medicationPanel); // 최종 패널에 추가

        // 총 투두리스트 달성도 라벨 생성 및 위치 지정 / 이후 DB연동과 달성도에 따라 칭호 및 이펙트 추가 예정
        JLabel todoRate = new JLabel("투두리스트 총 달성도: 80%", SwingConstants.LEFT);
        todoRate.setBounds(50, 450, 300, 30);
        todoRate.setFont(CommonStyle.BUTTON_FONT); // 공용 버튼 폰트
        add(todoRate); // 패널에 추가

        // 총 복약률 달성도 라벨 생성 및 위치 지정 / 이후 DB연동과 달성도에 따라 칭호 및 이펙트 추가 예정
        JLabel medRate = new JLabel("복약률 총 달성도: 90%", SwingConstants.LEFT);
        medRate.setBounds(50, 490, 300, 30);
        medRate.setFont(CommonStyle.BUTTON_FONT); // 공용 버튼 폰트
        add(medRate); // 패널에 추가

        // 오른쪽 아래 '홈으로' 버튼 생성 및 위치 지정 / 이후 공용 스타일로 하단레이아웃에 고정 예정
        JButton exitButton = new JButton("홈으로");
        exitButton.setBounds(650, 500, 80, 40);
        CommonStyle.stylePrimaryButton(exitButton); // 공통 스타일 적용
        exitButton.addActionListener(e -> {
            // TODO: 홈 화면으로 돌아가는 기능 구현 예정
            // 예: BaseFrame.showScreen(...) 호출 또는 현재 프레임 dispose() 호출
        });
        add(exitButton); // 패널에 추가
    }

    // 테스트용 메인 메서드 - 단독 실행 시 이 클래스를 JFrame에 띄움
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("통계 테스트");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null); // 화면 중앙 배치

            Statistics panel = new Statistics();
            frame.setContentPane(panel); // 프레임에 Statistics 패널 올리기

            frame.setVisible(true); // 화면에 표시
        });
    }
}
