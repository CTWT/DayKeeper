package pill;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 25.05.14
 * 파일명 : SupplementCard.java
 * 설명 : 개별 영양제 카드 구성 (이미지 + 이름 + 클릭 이벤트)
 */

public class SupplementCard extends JPanel {

    public SupplementCard(String name, ImageIcon image, Runnable onClick) {
        setPreferredSize(new Dimension(200, 220));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 이미지 라벨
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(image);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(180, 140));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 이름 라벨
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(imageLabel, BorderLayout.NORTH);
        add(nameLabel, BorderLayout.CENTER);

        // 클릭 이벤트
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.run();
            }
        });
    }
}
