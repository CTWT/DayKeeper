package pill;

import javax.swing.*;
import java.awt.*;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : 
 * 작성일 : 2025.05.14
 * 파일명 : SupApp.java
 */

// 메인 프레임 클래스: 전체 화면 전환과 초기화 담당
public class SupApp extends JFrame {
    private CardLayout cardLayout = new CardLayout(); // 화면 전환용 CardLayout
    private JPanel mainPanel = new JPanel(cardLayout); // 카드 레이아웃이 적용된 메인 패널

    // 생성자: 프레임 초기 설정 및 화면 패널 추가
    public SupApp() {
        setTitle("daykeeper"); // 윈도우 제목
        setSize(500, 600); // 윈도우 크기
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 닫기 버튼 누르면 종료
        setLocationRelativeTo(null); // 화면 중앙 정렬

        // 각 화면 패널 생성 (this = SupApp 참조 넘김)
        SupplementListPanel listPanel = new SupplementListPanel(this);
        AddSupplementPanel addPanel = new AddSupplementPanel(this);
        SupplementDetailPanel detailPanel = new SupplementDetailPanel(this);

        // 카드 레이아웃에 화면 등록
        mainPanel.add(listPanel, "list");     // 영양제 목록 화면
        mainPanel.add(addPanel, "add");       // 영양제 추가 화면
        mainPanel.add(detailPanel, "detail"); // 상세 보기 화면

        add(mainPanel); // 메인 패널을 프레임에 추가
        cardLayout.show(mainPanel, "list"); // 첫 화면은 목록 화면

        setVisible(true); // 화면 표시
    }

    // 화면 전환 메서드
    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    // 실행 시작점
    public static void main(String[] args) {
        new SupApp(); // 앱 실행
    }
}
