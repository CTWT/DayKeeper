package statistics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import common.Session;
import config.BaseFrame;
import config.ScreenType;
import login.Login;
import common.CommonStyle;

/**
 * 생성자 : 문원주
 * 생성일 : 25.05.19
 * 파일명 : Statistics.java
 * 수정자 :
 * 수정일 :
 * 설명 : 통계 패널들을 구성하여 화면에 띄우는 파일
 */

// 전체 통계 화면을 구성하는 메인 클래스
public class Statistics extends JPanel {

    private JPanel centerPanel; // 통계 패널들을 묶는 중앙 컨테이너

    public Statistics() {

        // 전체 패널 레이아웃을 BorderLayout으로 설정
        this.setLayout(new BorderLayout());
        this.setBackground(CommonStyle.BACKGROUND_COLOR);

        // NORTH: 상단 타이틀 라벨 추가
        JLabel titleLabel = CommonStyle.createTitleLabel(); // 공통 스타일의 타이틀 라벨 생성
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 가운데 정렬 패널
        panelTop.setBackground(CommonStyle.BACKGROUND_COLOR);
        panelTop.setBorder(BorderFactory.createEmptyBorder(-30, 0, 0, 0)); // 위치 수정
        panelTop.add(titleLabel);
        add(panelTop, BorderLayout.NORTH);

        // 중앙 컨테이너 패널 (Y축 정렬)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        // 1. 투두리스트 차트 → 중앙 정렬
        TodoChartPanel todoChart = new TodoChartPanel(Session.getUserId(), LocalDate.now());
        todoChart.setPreferredSize(new Dimension(700, 300));
        todoChart.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JPanel chartWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        chartWrapper.setBackground(CommonStyle.BACKGROUND_COLOR);
        chartWrapper.add(todoChart);
        centerPanel.add(chartWrapper);

        // 2. 복약 체크 → 중앙 정렬
        PillCheckPanel pillCheck = new PillCheckPanel(Session.getUserId());
        pillCheck.setPreferredSize(new Dimension(400, 48));
        JPanel pillWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pillWrapper.setBackground(CommonStyle.BACKGROUND_COLOR);
        pillWrapper.add(pillCheck);

        centerPanel.add(pillWrapper);

        // 3. 총 달성도 → 왼쪽 정렬
        TotalStatisticsPanel totalPanel = new TotalStatisticsPanel(Session.getUserId());
        JPanel totalWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 왼쪽 정렬
        totalWrapper.setBackground(CommonStyle.BACKGROUND_COLOR);
        totalWrapper.add(totalPanel);
        centerPanel.add(totalWrapper);

        // 전체 CENTER 영역에 추가
        this.add(centerPanel, BorderLayout.CENTER);

        // 하단 버튼 패널 추가 및 이벤트 연결
        CommonStyle.BottomPanelComponents bottom = CommonStyle.createBottomPanel();

        // 오늘 할일 상세보기 버튼 클릭 시 이벤트 처리
        bottom.todoDetail.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this); // 투두리스트창으로 이동
            frame.showScreen(ScreenType.TODOLIST);
        });

        // 영양제 정보 버튼 클릭 시 이벤트 처리
        bottom.pillDetail.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this); // 영양제창으로 이동
            frame.showScreen(ScreenType.PILL);
        });

        bottom.returnPage.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this); // 투두리스트창으로 이동
            frame.showScreen(ScreenType.TODOLIST);
        });

        // 버튼 상태 설정
        bottom.returnPage.setVisible(true); // 돌아가기 버튼 활성화
        bottom.statistics.setVisible(false); // 현재 페이지 버튼 비활성화

        // 하단 버튼 패널을 SOUTH에 추가
        this.add(bottom.panel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        BaseFrame frame = new BaseFrame(); // 기본 프레임 생성
        Statistics statisticsPanel = new Statistics(); // 통계 패널 생성
        frame.setContentPane(statisticsPanel); // 패널을 프레임에 설정
        frame.setVisible(true); // 화면 표시
    }

}
