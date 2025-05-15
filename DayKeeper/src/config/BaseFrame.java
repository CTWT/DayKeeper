package config;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.12
 * 파일명 : BaseFrame.java
 * 수정자 : 
 * 수정일 :
 * 설명 : swing 기본 출력화면 사이즈 지정
 */
public class BaseFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private Map<String, JPanel> screenMap = new HashMap<>();

    public BaseFrame() {

        // 타이틀 설정
        setTitle("DayKeeper");

        // 고정 크기 설정
        setSize(800, 600);

        // 창 크기 고정
        setResizable(false);

        // 닫기 시 종료
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 가운데 정렬∏
        setLocationRelativeTo(null);

        add(cardPanel);

        showScreen(ScreenType.LOGIN);

        setVisible(true);

    }

    public void showScreen(ScreenType screenType) {
        String name = screenType.getScreenName();

        if (!screenMap.containsKey(name)) {
            try {
                // 리플렉션으로 화면 클래스 생성
                JPanel panel = screenType.getPanelClass().getDeclaredConstructor().newInstance();
                screenMap.put(name, panel);
                cardPanel.add(panel, name);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "화면생성실패 : " + name);
                return;
            }
        }
        cardLayout.show(cardPanel, name);
    }
}
