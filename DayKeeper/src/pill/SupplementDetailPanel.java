package pill;

import javax.swing.*;
import java.awt.*;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 25.05.14
 * 파일명 : SupplementDetailPanel.java
 * 설명 : 선택한 영양제의 상세 정보를 보여주는 전용 패널
 */

public class SupplementDetailPanel extends JPanel {
    private JLabel titleLabel;
    private JTextArea detailArea;

    public SupplementDetailPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        titleLabel = new JLabel("영양제 상세 정보", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        detailArea = new JTextArea("상세 내용이 여기에 표시됩니다.");
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setEditable(false);
        detailArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailArea.setMargin(new Insets(20, 20, 20, 20));
        add(new JScrollPane(detailArea), BorderLayout.CENTER);
    }

    public void loadSupplementInfo(String name) {
        titleLabel.setText(name + " 상세 정보");
        detailArea.setText("[" + name + "]에 대한 자세한 설명을 여기에 출력합니다.");
    }
}
