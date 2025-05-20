package todoDetail;

import javax.swing.JFrame;
import common.CommonStyle;;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.18
 * 파일명 : TodoDetail.java
 * 수정자 : 
 * 수정일 :
 * 설명 : Detail 관련 3개의 창 기본 설정 및 컨트롤을 담당하는 메인 프레임 클래스
 */

public class TodoDetail extends JFrame {
    // 관리할 각 패널
    private DetailMain mainPanel;


    /**
     * DetailMainFrame 기본 생성자.
     * 프레임 크기 및 종료 동작 설정, 패널 초기화 및 CardLayout 설정을 수행한다.
     */
    public TodoDetail() {
        CommonStyle.createTitleLabel();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new DetailMain();

        add(mainPanel);
        setVisible(true);
    }

    

    /**
     * 애플리케이션 진입점 메인 메서드.
     * DetailMainFrame 인스턴스를 생성하여 프로그램을 시작한다.
     * 
     * @param args 커맨드라인 인수 (사용하지 않음)
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TodoDetail();
        });
    }

}

// Detailtodolist -> todoDetail 패키지
// TodoDetailMain 만들기 -> todoDetail.java
// UI 더더더 이쁘게 제작하기! - 리스트에 줄 긋기

