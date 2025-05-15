package pill;

import javax.swing.*;
import java.awt.*;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 25.05.14
 * 파일명 : SupApp.java
 * 설명 : 메인 프레임. CardLayout으로 화면 전환을 제어함
 */

public class SupApp extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    private SupplementListPanel listPanel;
    private AddSupplementPanel addPanel;
    private SupplementDetailPanel detailPanel;

    public SupApp() {
        setTitle("daykeeper");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 화면 구성
        listPanel = new SupplementListPanel(this);
        addPanel = new AddSupplementPanel();
        detailPanel = new SupplementDetailPanel();

        mainPanel.add(listPanel, "list");
        mainPanel.add(addPanel, "add");
        mainPanel.add(detailPanel, "detail");

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "list");
    }

    // 상세 보기 패널로 이동
    public void showAddSupplementPanel(String name) {
        addPanel.loadSupplementInfo(name); // 등록 또는 보기 용도
        cardLayout.show(mainPanel, "add");
    }

    public void showDetailPanel(String name) {
        detailPanel.loadSupplementInfo(name);
        cardLayout.show(mainPanel, "detail");
    }

    public void showListPanel() {
        cardLayout.show(mainPanel, "list");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupApp().setVisible(true));
    }
}
