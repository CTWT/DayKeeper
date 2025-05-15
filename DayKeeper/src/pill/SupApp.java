package pill;

import javax.swing.*;
import java.awt.*;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 25.05.15
 * 파일명 : SupApp.java
 * 설명 : 전체 화면을 관리하는 메인 프레임 (화면 전환 포함)
 */

public class SupApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private SupplementListPanel listPanel;
    private SupplementDetailPanel detailPanel;
    private AddSupplementPanel addPanel;

    public SupApp() {
        setTitle("daykeeper");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        listPanel = new SupplementListPanel(this);
        detailPanel = new SupplementDetailPanel(this);
        addPanel = new AddSupplementPanel(this);

        mainPanel.add(listPanel, "list");
        mainPanel.add(detailPanel, "detail");
        mainPanel.add(addPanel, "add");

        add(mainPanel);
        cardLayout.show(mainPanel, "list");
    }

    public void showPanel(String name){
        cardLayout.show(mainPanel,name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupApp().setVisible(true));
    }
}
