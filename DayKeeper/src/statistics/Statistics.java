package statistics;

import common.CommonStyle;
import common.Session;
import config.BaseFrame;
import login.Login;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.text.NumberFormat;

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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 생성자 : 문원주
 * 생성일 : 25.05.19
 * 파일명 : Statistics.java
 * 수정자 :
 * 수정일 :
 * 설명 : 투두리스트와 영양제 복용률을 체크하여 표시하는 통계 페이지
 */

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

        // CENTER: 중앙에 차트와 복약 패널 구성

        // DefaultCategoryDataset weeklyTodoDataset =
        // createTodoDataset(Session.getUserId(),
        // LocalDate.now());
        // 2025년 5월 12일을 기준 날짜로 설정 (이 주 월요일)
        DefaultCategoryDataset weeklyTodoDataset = createTodoDataset("testuser", LocalDate.of(2025, 5, 12));

        // 선형 차트 생성
        JFreeChart lineChart = ChartFactory.createLineChart(
                "주간 투두리스트 달성도", "", "달성률(%)", weeklyTodoDataset,
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

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 100); // Y축 범위 0~100%
        rangeAxis.setTickUnit(new NumberTickUnit(10)); // 10% 단위 눈금 설정

        // 점선용 스트로크 정의 (선 길이 5, 공백 5) - CASE_1 선택
        float[] dashPattern = { 5.0f, 5.0f };
        BasicStroke dashedStroke = new BasicStroke(
                1.0f, // 선 굵기
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f,
                dashPattern,
                0.0f);

        // 격자선에 점선 스타일 적용
        plot.setDomainGridlineStroke(dashedStroke); // x축 격자선 (수직선)
        plot.setRangeGridlineStroke(dashedStroke); // y축 격자선 (수평선)

        // 격자선 색깔도 설정 가능 (원하는 색상으로 변경)
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        // 격자선 보이게 설정 (기본적으로 보임이지만 혹시 꺼져있으면 켜기)
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);

        // 차트 선 두껍게 설정 및 점 제거
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3.5f)); // 1.0f로 설정된 선을 더 굵게 표현
        renderer.setSeriesShapesVisible(0, true);

        // 값 표시 활성화 (점 위에 숫자 보임) - CASE_2 선택
        renderer.setDefaultItemLabelsVisible(true);

        // 값 표시 형식 지정 (예: 소수점 1자리, % 붙임)
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                "{2}%", NumberFormat.getNumberInstance()));

        // 값 글꼴, 색상도 설정 가능
        renderer.setDefaultItemLabelFont(CHART_FONT);
        renderer.setDefaultItemLabelPaint(Color.BLACK);
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

        // 상단: 복약 여부 아이콘으로 표시
        Boolean[] status = new statistics.PhillDAO().getWeeklyMedicationStatus(Session.getUserId());
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        for (int i = 0; i < 7; i++) {
            JPanel phillbox = new JPanel();
            phillbox.setPreferredSize(new Dimension(40, 40));
            phillbox.setOpaque(false);
            JLabel symbolLabel = new JLabel();
            symbolLabel.setHorizontalAlignment(SwingConstants.CENTER);
            symbolLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

            phillbox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            LocalDate targetDate = monday.plusDays(i);

            if (status[i] == null) {
                if (targetDate.isBefore(today)) {
                    phillbox.setBackground(new Color(242, 242, 242)); // 미복약 색상
                    phillbox.setOpaque(true);
                    symbolLabel.setText("✗");
                    symbolLabel.setForeground(Color.DARK_GRAY);
                } else {
                    phillbox.setBackground(Color.WHITE);
                    phillbox.setOpaque(true);
                    symbolLabel.setText("-");
                    symbolLabel.setForeground(Color.GRAY);
                }
            } else if (status[i]) {
                phillbox.setBackground(new Color(76, 154, 255)); // 복약함: 파란 원
                phillbox.setOpaque(true);
                symbolLabel.setText("✓");
                symbolLabel.setForeground(Color.WHITE);
            } else {
                phillbox.setBackground(new Color(242, 242, 242)); // 미복약: 연회색 원
                phillbox.setOpaque(true);
                symbolLabel.setText("✗");
                symbolLabel.setForeground(Color.DARK_GRAY);
            }

            phillbox.setLayout(new java.awt.GridBagLayout()); // 가운데 정렬
            phillbox.add(symbolLabel);
            medicationPanel.add(phillbox);
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

        // String userId = Session.getUserId();
        String userId = "testuser";
        StatisticsTodoDAO todoDAO = new StatisticsTodoDAO();
        PhillDAO pillDAO = new PhillDAO();

        double todoRateValue = todoDAO.getTotalTodo(userId);
        double pillRateValue = pillDAO.getTotalPill(userId);

        JLabel todoRate = new JLabel("투두리스트 총 달성도: " + (int) todoRateValue + "%");
        JLabel medRate = new JLabel("복약률 총 달성도: " + (int) pillRateValue + "%");
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

    private DefaultCategoryDataset createTodoDataset(String userId, LocalDate referenceDate) {
        StatisticsTodoDAO dao = new StatisticsTodoDAO();
        Map<String, Double> weeklyRates = dao.getWeeklyRate(userId, referenceDate);

        DefaultCategoryDataset weeklyTodoDataset = new DefaultCategoryDataset();
        for (String day : List.of("월", "화", "수", "목", "금", "토", "일")) {
            double rate = weeklyRates.getOrDefault(day, 0.0);
            weeklyTodoDataset.addValue(rate, "달성률", day);
        }

        return weeklyTodoDataset;
    }

    public static void main(String[] args) {
        BaseFrame b = new BaseFrame(); // 기본 프레임 생성
        Statistics s = new Statistics(); // 통계 패널 생성
        b.setContentPane(s); // 프레임에 통계 패널 설정
        b.setVisible(true); // 프레임 보이기
    }
}
