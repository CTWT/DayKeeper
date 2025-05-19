package statistics;

import common.CommonStyle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 생성자 : 문원주
 * 생성일 : 25.05.19
 * 파일명 : TodoChartPanel.java
 * 수정자 :
 * 수정일 :
 * 설명 : 투두리스트 DAO에서 값을 받아와 차트로 그리는 파일
 */

// 주간 투두리스트 달성률 라인 차트를 그리는 패널 클래스
public class TodoChartPanel extends JPanel {

    // 차트 및 라벨에 사용할 기본 폰트 정의
    private static final Font CHART_FONT = new Font("SansSerif", Font.PLAIN, 14);

    /**
     * 생성자 - 패널 초기화 및 차트 생성
     * 
     * @param userId        사용자 아이디
     * @param referenceDate 기준 날짜 (해당 주간 기준)
     */
    public TodoChartPanel(String userId, LocalDate referenceDate) {
        setBackground(CommonStyle.BACKGROUND_COLOR); // 배경색 설정
        setLayout(new BorderLayout()); // 패널 레이아웃 설정

        // 데이터셋 생성
        DefaultCategoryDataset dataset = createTodoDataset(userId, referenceDate);

        // 차트 생성
        JFreeChart lineChart = createLineChart(dataset);

        // 차트를 포함하는 ChartPanel 생성 및 설정
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(700, 300));
        chartPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        // 패널에 차트 추가
        add(chartPanel, BorderLayout.CENTER);
    }

    // DAO를 통해 투두리스트 달성률 데이터를 가져와 데이터셋 생성
    private DefaultCategoryDataset createTodoDataset(String userId, LocalDate referenceDate) {
        StatisticsTodoDAO dao = new StatisticsTodoDAO(); // DAO 객체 생성
        Map<String, Double> weeklyRates = dao.getWeeklyRate(userId, referenceDate); // 요일별 달성률 데이터 조회

        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); // 차트용 데이터셋 생성

        // 요일 순서대로 데이터셋에 값 추가
        for (String day : List.of("월", "화", "수", "목", "금", "토", "일")) {
            double rate = weeklyRates.getOrDefault(day, 0.0); // 값이 없으면 0.0
            dataset.addValue(rate, "달성률", day); // 차트에 데이터 추가
        }

        return dataset;
    }

    // 주어진 데이터셋을 기반으로 라인 차트를 생성
    private JFreeChart createLineChart(DefaultCategoryDataset dataset) {
        // 라인 차트 생성
        JFreeChart chart = ChartFactory.createLineChart(
                "주간 투두리스트 달성도", // 차트 제목
                "", // X축 레이블 생략
                "달성률(%)", // Y축 레이블
                dataset, // 데이터셋
                PlotOrientation.VERTICAL, // 세로 방향
                false, // 범례 사용 안함
                true, // 툴팁 사용
                false // URL 링크 사용 안함
        );

        chart.getTitle().setFont(CHART_FONT); // 차트 제목 폰트 설정
        chart.setBackgroundPaint(CommonStyle.BACKGROUND_COLOR); // 차트 배경색 설정

        // 플롯 영역 커스터마이징
        CategoryPlot plot = chart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(CHART_FONT); // X축 라벨 폰트
        plot.getRangeAxis().setLabelFont(CHART_FONT); // Y축 라벨 폰트
        plot.getDomainAxis().setTickLabelFont(CHART_FONT); // X축 눈금 폰트
        plot.getRangeAxis().setTickLabelFont(CHART_FONT); // Y축 눈금 폰트
        plot.setBackgroundPaint(CommonStyle.BACKGROUND_COLOR); // 플롯 배경색 설정

        // Y축 설정
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(); // Y축 객체 가져오기
        rangeAxis.setRange(0, 100); // 0~100 범위로 설정
        rangeAxis.setTickUnit(new NumberTickUnit(10)); // 10 단위 눈금 설정

        // 격자선(눈금선) 스타일 설정
        float[] dashPattern = { 5.0f, 5.0f }; // 점선 패턴
        BasicStroke dashedStroke = new BasicStroke(
                1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                10.0f, dashPattern, 0.0f);
        plot.setDomainGridlineStroke(dashedStroke); // X축 격자선
        plot.setRangeGridlineStroke(dashedStroke); // Y축 격자선
        plot.setDomainGridlinePaint(Color.GRAY); // X축 격자선 색상
        plot.setRangeGridlinePaint(Color.GRAY); // Y축 격자선 색상
        plot.setDomainGridlinesVisible(true); // X축 격자선 표시 여부
        plot.setRangeGridlinesVisible(true); // Y축 격자선 표시 여부

        // 선과 마커, 라벨 표시 설정
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3.5f)); // 선 굵기
        renderer.setSeriesShapesVisible(0, true); // 마커 표시
        renderer.setDefaultItemLabelsVisible(true); // 라벨 표시 여부
        // 값 표시 형식 지정 (예: 소수점 1자리, % 붙임)
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                "{2}%", NumberFormat.getNumberInstance()));
        renderer.setDefaultItemLabelFont(CHART_FONT); // 라벨 폰트 설정
        renderer.setDefaultItemLabelPaint(Color.BLACK); // 라벨 색상 설정
        plot.setRenderer(renderer); // 렌더러 적용

        return chart;
    }
}
