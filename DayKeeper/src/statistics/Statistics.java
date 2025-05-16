package statistics;

import common.CommonStyle;
import config.BaseFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class Statistics extends JPanel {
    public static final Font CHART_FONT = new Font("SansSerif", Font.PLAIN, 14); // 차트 폰트

    public Statistics() {
        setLayout(new BorderLayout()); // 전체 패널의 레이아웃 설정
        setBackground(CommonStyle.BACKGROUND_COLOR); // 배경색 설정

        // NORTH: 상단 타이틀 라벨 추가
        JLabel titleLabel = CommonStyle.createTitleLabel(); // 공통 스타일의 타이틀 라벨 생성
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 가운데 정렬 패널
        panelTop.setBackground(CommonStyle.BACKGROUND_COLOR);
        panelTop.add(titleLabel);
        add(panelTop, BorderLayout.NORTH);

        // CENTER: 중앙에 차트와 복약 패널 구성

        // 주간 투두리스트 달성률 데이터셋 구성 - 이후 DB 연동으로 수정 계획
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(80, "달성률", "월");
        dataset.addValue(60, "달성률", "화");
        dataset.addValue(20, "달성률", "수");
        dataset.addValue(70, "달성률", "목");
        dataset.addValue(50, "달성률", "금");
        dataset.addValue(40, "달성률", "토");
        dataset.addValue(85, "달성률", "일");

        // 선형 차트 생성
        JFreeChart lineChart = ChartFactory.createLineChart(
                "주간 투두리스트 달성도", "", "달성률(%)", dataset,
                PlotOrientation.VERTICAL, false, true, false);
        lineChart.getTitle().setFont(CHART_FONT); // 제목 폰트 설정
        lineChart.setBackgroundPaint(CommonStyle.BACKGROUND_COLOR); // 배경색

        // 차트 plot 설정
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(CHART_FONT); // X축 라벨 폰트
        plot.getRangeAxis().setLabelFont(CHART_FONT); // Y축 라벨 폰트
        plot.getDomainAxis().setTickLabelFont(CHART_FONT); // X축 눈금 폰트
        plot.getRangeAxis().setTickLabelFont(CHART_FONT); // Y축 눈금 폰트
        plot.setBackgroundPaint(CommonStyle.BACKGROUND_COLOR); // 차트 내부 배경
        plot.getRangeAxis().setRange(0, 100); // Y축 범위 설정

        // 차트 선 두껍게 설정 및 점 제거
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3.5f)); // 1.0f로 설정된 선을 더 굵게 표현
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        // 차트를 담는 패널
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(700, 300)); // 차트 크기 설정
        chartPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        // ────────────────────────────────────────
        // 복약 상태 패널 (요일 및 복약 성공 여부)

        JPanel medicationPanel = new JPanel(new GridLayout(2, 7)); // 2행 7열 그리드 (상태 + 요일)
        medicationPanel.setPreferredSize(new Dimension(400, 48)); // 차트크기보다 줄여서 설정
        medicationPanel.setBackground(Color.WHITE); // 배경색 흰색

        // 상단: 복약 여부 표시 박스 (색상으로 구분) - 이후 기능 수정과 다지인 수정 및 DB연동 예정
        for (int i = 0; i < 7; i++) {
            JLabel medStatus = new JLabel();
            medStatus.setOpaque(true);
            medStatus.setBackground(i % 2 == 0 ? new Color(100, 255, 100) : new Color(255, 64, 64)); // 초록 또는 빨강
            if (i == 5 || i == 6) {
                medStatus.setBackground(Color.WHITE); // 주말은 흰색
            }
            medStatus.setBorder(new LineBorder(Color.BLACK)); // 테두리 추가
            medicationPanel.add(medStatus);
        }

        // 하단: 요일 라벨
        String[] days = { "월", "화", "수", "목", "금", "토", "일" };
        for (String day : days) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(CommonStyle.TEXT_FONT);
            medicationPanel.add(dayLabel);
        }

        // 복약 패널을 중앙 정렬로 감싸는 래퍼 패널
        JPanel medicationWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        medicationWrapper.setBackground(CommonStyle.BACKGROUND_COLOR);
        medicationWrapper.add(medicationPanel);

        // 차트와 복약 패널, 달성도패널을 담는 centerPanel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(CommonStyle.BACKGROUND_COLOR);
        centerPanel.add(chartPanel, BorderLayout.NORTH); // 상단: 차트
        centerPanel.add(medicationWrapper, BorderLayout.CENTER); // 중앙: 복약 패널

        JLabel todoRate = new JLabel("투두리스트 총 달성도: 80%");
        JLabel medRate = new JLabel("복약률 총 달성도: 90%");
        todoRate.setFont(CommonStyle.BUTTON_FONT);
        medRate.setFont(CommonStyle.BUTTON_FONT);

        JPanel panelRate = new JPanel();
        panelRate.setLayout(new BoxLayout(panelRate, BoxLayout.Y_AXIS)); // 수직 정렬
        panelRate.setBackground(CommonStyle.BACKGROUND_COLOR);
        panelRate.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelRate.add(todoRate);
        panelRate.add(Box.createRigidArea(new Dimension(0, 5))); // 간격
        panelRate.add(medRate);

        centerPanel.add(panelRate, BorderLayout.SOUTH); // 하단에 달성도 추가

        add(centerPanel, BorderLayout.CENTER); // CENTER 전체 배치

        // 하단 버튼 패널 추가
        CommonStyle.BottomPanelComponents bottom = CommonStyle.createBottomPanel();

        // 버튼 이벤트 처리
        bottom.todoDetail.addActionListener(e -> {
            System.out.println("오늘할일상세보기 클릭됨");
        });
        bottom.pillDetail.addActionListener(e -> {
            System.out.println("영양제 정보 클릭됨");
        });

        bottom.returnPage.setVisible(true); // 돌아가기 버튼 활성화
        bottom.statistics.setVisible(false); // 현재 페이지 버튼은 비활성화
        add(bottom.panel, BorderLayout.SOUTH); // SOUTH에 추가
    }

    public static void main(String[] args) {
        BaseFrame b = new BaseFrame(); // 기본 프레임 생성
        Statistics s = new Statistics(); // 통계 패널 생성
        b.setContentPane(s); // 프레임에 통계 패널 설정
        b.setVisible(true); // 프레임 보이기
    }
}
