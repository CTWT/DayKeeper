package DetailTodoList;

import java.awt.CardLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.18
 * 파일명 : DetailMainFrame.java
 * 수정자 : 
 * 수정일 :
 * 설명 : Detail 관련 3개의 창 기본 설정 및 컨트롤을 담당하는 메인 프레임 클래스
 */

public class DetailMainFrame extends JFrame {
    // 관리할 각 패널
    private DetailMainPanel mainPanel;


    /**
     * DetailMainFrame 기본 생성자.
     * 프레임 크기 및 종료 동작 설정, 패널 초기화 및 CardLayout 설정을 수행한다.
     */
    public DetailMainFrame() {
        setTitle("DAY-KEEPER");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new DetailMainPanel();

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
            new DetailMainFrame();
        });
    }

}

