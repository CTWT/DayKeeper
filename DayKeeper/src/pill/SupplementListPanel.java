package pill;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : 김관호
 * 수정일 : 2025.05.15
 * 파일명 : SupplementListPanel.java
 */

public class SupplementListPanel extends JPanel {
    private SupApp app;

    public SupplementListPanel(SupApp app) {
        this.app = app;

        PillManager.getInst().LoadDBData();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("등록된 영양제", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        HashMap<Integer, PillDTO> pillsMap = PillManager.getInst().getPillsMap();
        Iterator<Integer> iterator = pillsMap.keySet().iterator();
        int col = 0, row = 0;
        while (iterator.hasNext()) {
            Integer id = iterator.next();
            gbc.gridx = col;
            gbc.gridy = row;
            gridPanel.add(createPillCard(id), gbc);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        JPanel centerPanel = new JPanel(flowLayout);
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(gridPanel);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(245, 245, 245));
        JButton addBtn = new JButton("➕ 추가");
        JButton homeBtn = new JButton("🏠 처음으로");

        Font btnFont = new Font("맑은 고딕", Font.PLAIN, 13);
        addBtn.setFont(btnFont);
        homeBtn.setFont(btnFont);

        addBtn.addActionListener(e -> app.showPanel("add"));
        homeBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "처음으로 돌아갑니다."));

        bottom.add(addBtn);
        bottom.add(homeBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel createPillCard(Integer pillId) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(new Color(245, 245, 245));
        wrapper.setPreferredSize(new Dimension(160, 200));

        String pillName = PillManager.getInst().getDatabyId(pillId).getPillName();
        int[] count = {5};

        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(new Color(245, 245, 245));
        JLabel nameLabel = new JLabel(pillName);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        JLabel countLabel = new JLabel("남은 수량: " + count[0]);
        countLabel.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        labelPanel.add(nameLabel, BorderLayout.WEST);
        labelPanel.add(countLabel, BorderLayout.EAST);

        JPanel card = new JPanel(null);
        card.setPreferredSize(new Dimension(150, 150));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        try {
            Image image = ResourcesManager.getInst().getImagebyName(pillName);
            Image scaledImage = image.getScaledInstance(145, 145, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
            iconLabel.setBounds(2, 2, 145, 145);

            iconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    app.setDetailId(pillId);
                    app.showPanel("detail");
                }
            });

            JCheckBox checkBox = new JCheckBox();
            checkBox.setBounds(120, 8, 22, 22);
            checkBox.setOpaque(true);
            checkBox.setBackground(Color.WHITE);
            checkBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            checkBox.setToolTipText("복용 체크");

            checkBox.addActionListener(e -> {
                if (count[0] > 0) {
                    count[0]--;
                    countLabel.setText("남은 수량: " + count[0]);
                } else {
                    JOptionPane.showMessageDialog(card, "더 이상 수량이 없습니다.");
                }
                checkBox.setSelected(false);
            });

            card.add(iconLabel);
            card.add(checkBox);
            card.setComponentZOrder(checkBox, 0);

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("이미지 없음", SwingConstants.CENTER);
            errorLabel.setBounds(10, 60, 130, 30);
            card.add(errorLabel);
        }

        wrapper.add(labelPanel);
        wrapper.add(Box.createRigidArea(new Dimension(0, 5)));
        wrapper.add(card);

        return wrapper;
    }
}
