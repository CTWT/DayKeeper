package statistics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import common.Session;
import config.BaseFrame;
import config.ScreenType;
import common.CommonStyle;

/**
 * 생성자 : 문원주
 * 생성일 : 25.05.19
 * 파일명 : Statistics.java
 * 수정자 : 문원주
 * 수정일 : 25.05.20
 * 설명 : 콤보박스를 이용하여 이전 데이터들을 불러오게 구상
 */

// 전체 통계 화면을 구성하는 메인 클래스
public class Statistics extends JPanel {

    private JPanel centerPanel; // 중앙 콘텐츠 영역을 필드로 분리

    // 생성자 : 화면 초기 구성 및 이벤트 설정
    public Statistics() {

        this.setLayout(new BorderLayout()); // 전체 레이아웃은 BorderLayout 사용
        this.setBackground(CommonStyle.BACKGROUND_COLOR); // 공통 배경색 설정

        // ─── NORTH 구성 (타이틀 + 주차 선택 콤보박스) ───
        JLabel titleLabel = CommonStyle.createTitleLabel(); // 상단 타이틀 라벨 생성
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 타이틀 패널 중앙 정렬
        titlePanel.setBackground(CommonStyle.BACKGROUND_COLOR); // 배경색 설정
        titlePanel.add(titleLabel); // 타이틀 라벨 추가

        String[] weekOptions = { "이번 주", "지난 주", "2주 전", "3주 전" }; // 콤보박스 선택지 배열
        JComboBox<String> weekSelector = new JComboBox<>(weekOptions); // 콤보박스 생성
        weekSelector.setPreferredSize(new Dimension(100, 25)); // 콤보박스 크기 지정
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // 콤보박스 패널 우측 정렬
        comboPanel.setBackground(CommonStyle.BACKGROUND_COLOR); // 배경색 설정
        comboPanel.add(weekSelector); // 콤보박스 추가

        JPanel panelTop = new JPanel(); // 상단 전체 패널
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS)); // 수직 정렬
        panelTop.setBackground(CommonStyle.BACKGROUND_COLOR); // 배경색 설정
        panelTop.setBorder(BorderFactory.createEmptyBorder(-20, 0, 0, 0)); // 상단 간격 조정
        panelTop.add(titlePanel); // 타이틀 패널 추가
        panelTop.add(comboPanel); // 콤보박스 패널 추가

        this.add(panelTop, BorderLayout.NORTH); // 상단 패널 NORTH에 배치

        // ─── CENTER 구성 (통계 패널들 표시 영역) ───
        centerPanel = new JPanel(); // 중앙 패널 생성
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // 수직 정렬
        centerPanel.setBackground(CommonStyle.BACKGROUND_COLOR); // 배경색 설정
        this.add(centerPanel, BorderLayout.CENTER); // CENTER에 중앙 패널 추가

        updateData(LocalDate.now()); // 초기 화면 데이터 표시 (이번 주 기준)

        // ─── 콤보박스 이벤트 처리 (주차 선택 시 데이터 갱신) ───
        weekSelector.addActionListener(e -> {
            String selected = (String) weekSelector.getSelectedItem(); // 선택된 항목 가져오기
            LocalDate baseDate = LocalDate.now(); // 기준 날짜는 오늘로 설정

            // 선택에 따라 기준 날짜를 조정
            if (selected.equals("지난 주")) {
                baseDate = baseDate.minusWeeks(1);
            } else if (selected.equals("2주 전")) {
                baseDate = baseDate.minusWeeks(2);
            } else if (selected.equals("3주 전")) {
                baseDate = baseDate.minusWeeks(3);
            }

            updateData(baseDate); // 변경된 기준 날짜로 데이터 갱신
        });

        // ─── SOUTH 구성 (화면 하단 버튼들) ───
        CommonStyle.BottomPanelComponents bottom = CommonStyle.createBottomPanel(); // 공통 하단 버튼 패널 구성

        // 메인화면 버튼 클릭 시 기본 화면(투두)로 전환
        bottom.todoList.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this); // 상위 프레임 얻기
            frame.showScreen(ScreenType.TODOLIST); // 투두 화면으로 이동
        });

        // 복약 상세 버튼 클릭 시 복약 화면으로 전환
        bottom.pillDetail.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this); // 상위 프레임 얻기
            frame.showScreen(ScreenType.PILL); // 복약 화면으로 이동
        });

        // 돌아가기 버튼 클릭 시 기본 화면(투두)으로 전환
        bottom.returnPage.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this); // 상위 프레임 얻기
            frame.showScreen(ScreenType.TODOLIST); // 투두 화면으로 이동
        });

        bottom.todoList.setVisible(true); // 메인 화면 버튼 추가
        bottom.pillDetail.setVisible(true); // 영양제 화면 버튼 추가
        bottom.returnPage.setVisible(true); // 돌아가기 버튼 추가

        this.add(bottom.panel, BorderLayout.SOUTH); // SOUTH에 하단 패널 추가
    }

    /**
     * 주어진 기준일(baseDate)에 따라 centerPanel에 표시될 콘텐츠를 갱신함
     */
    private void updateData(LocalDate baseDate) {
        centerPanel.removeAll(); // 기존 컴포넌트 제거

        // 1. 투두 차트 구성
        TodoChartPanel todoChart = new TodoChartPanel(Session.getUserId(), baseDate); // 사용자 ID와 기준일 전달
        todoChart.setPreferredSize(new Dimension(700, 300)); // 차트 크기 설정
        todoChart.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // 여백 설정
        JPanel chartWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 차트 감싸는 패널
        chartWrapper.setBackground(CommonStyle.BACKGROUND_COLOR); // 배경색 설정
        chartWrapper.add(todoChart); // 차트 추가
        centerPanel.add(chartWrapper); // centerPanel에 추가

        // 2. 복약 체크 패널 구성
        PillCheckPanel pillPanel = new PillCheckPanel(Session.getUserId(), baseDate); // 사용자 ID와 기준일 전달
        pillPanel.setPreferredSize(new Dimension(400, 48)); // 크기 설정
        JPanel pillWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 감싸는 패널
        pillWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, -10, 0)); // 여백 설정
        pillWrapper.setBackground(CommonStyle.BACKGROUND_COLOR); // 배경색 설정
        pillWrapper.add(pillPanel); // 복약 패널 추가
        centerPanel.add(pillWrapper); // centerPanel에 추가

        // 3. 총 달성도 패널 구성
        TotalStatisticsPanel totalPanel = new TotalStatisticsPanel(Session.getUserId(), baseDate); // 사용자 ID와 기준일 전달
        JPanel totalWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 감싸는 패널
        totalWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, -10, 0)); // 여백 설정
        totalWrapper.setBackground(CommonStyle.BACKGROUND_COLOR); // 배경색 설정
        totalWrapper.add(totalPanel); // 총 통계 패널 추가
        centerPanel.add(totalWrapper); // centerPanel에 추가

        centerPanel.revalidate(); // 레이아웃 갱신
        centerPanel.repaint(); // 다시 그리기
    }
}
