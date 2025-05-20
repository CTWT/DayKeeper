package pill.pillPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import common.CommonStyle;
import config.BaseFrame;
import config.ScreenType;
import pill.pillDAO.PillDAO;
import pill.pillDAO.PillYnDAO;
import pill.pillManager.PillDTO;
import pill.pillManager.PillManager;
import pill.pillManager.ResourcesManager;

/*
 * 작성자 : 임해균
 * 작성일 : 2025.05.16
 * 수정자 : 김관호
 * 수정일 : 2025.05.20
 * 파일명 : Pill.java
 * 설명 : 전체 영양제 목록을 카드 형식으로 보여주는 패널
 */

enum ModalName {
    DETAIL,
    ADD,
    TIMESETTING,
}

public class Pill extends JPanel {
    private Map<Integer, JLabel> countLabelMap = new HashMap<>();
    private Integer detail_id;
    JPanel centerPanel;

    public Pill() {
        setLayout(new BorderLayout());
        setBackground(CommonStyle.BACKGROUND_COLOR);

        update();

        // 🟦 타이틀 패널 (DAY-KEEPER + 좌측 정렬된 '등록된 영양제')
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("DAY-KEEPER", SwingConstants.CENTER);
        titleLabel.setFont(CommonStyle.TITLE_FONT);
        titleLabel.setForeground(CommonStyle.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel subTitleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        subTitleWrapper.setBackground(CommonStyle.BACKGROUND_COLOR);

        JLabel subTitleLabel = new JLabel("등록된 영양제");
        subTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        subTitleWrapper.add(subTitleLabel);

        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(subTitleWrapper);

        add(titlePanel, BorderLayout.NORTH);

        // 🟦 스크롤 영역
        centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        centerPanel.setBackground(CommonStyle.BACKGROUND_COLOR);
        centerPanel.add(createGrid());

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(40);
        add(scrollPane, BorderLayout.CENTER);

        // 🟦 하단 버튼 패널
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottom.setBackground(CommonStyle.BACKGROUND_COLOR);

        JButton addBtn = new JButton("➕ 추가");
        JButton homeBtn = new JButton("🏠 처음으로");
        JButton timeBtn = new JButton("⏱ 시간 설정");
        JButton consumeBtn = new JButton("💊 영양제 섭취");

        for (JButton btn : Arrays.asList(addBtn, homeBtn, timeBtn, consumeBtn)) {
            btn.setFont(CommonStyle.TEXT_FONT);
            CommonStyle.stylePrimaryButton(btn);
            bottom.add(btn);
        }

        addBtn.addActionListener(e -> OpenModal(ModalName.ADD));
        homeBtn.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.TODOLIST);
        });
        timeBtn.addActionListener(e -> OpenModal(ModalName.TIMESETTING));

        consumeBtn.addActionListener(e -> {
            if (new PillYnDAO().checkConsume()) {
                JOptionPane.showMessageDialog(this, "오늘은 이미 영양제를 섭취했습니다.");
            } else {
                for (Integer id : PillManager.getInst().getPillsMap().keySet()) {
                    new PillDAO().consumePill(id, 1);
                    updateCountLabel(id);
                }
                JOptionPane.showMessageDialog(this, "전체 영양제를 1개씩 섭취 처리했습니다.");
                new PillYnDAO().changeYnToDB("Y");
                update();
            }
        });

        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel createPillCard(Integer pillId) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(CommonStyle.BACKGROUND_COLOR);
        wrapper.setPreferredSize(new Dimension(160, 200));

        String pillName = PillManager.getInst().getDataById(pillId).getPillName();
        int amount = new PillDAO().getPillAmount(pillId);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(CommonStyle.BACKGROUND_COLOR);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel nameLabel = new JLabel(pillName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countLabel = new JLabel("남은 수량: " + amount, SwingConstants.CENTER);
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        countLabel.setForeground(CommonStyle.PRIMARY_COLOR);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        countLabelMap.put(pillId, countLabel);

        labelPanel.add(nameLabel);
        labelPanel.add(Box.createVerticalStrut(3));
        labelPanel.add(countLabel);

        JPanel card = new JPanel(null);
        card.setPreferredSize(new Dimension(150, 150));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        try {
            Image image = ResourcesManager.getInst().getImagebyName(pillName);
            Image scaledImage = image.getScaledInstance(145, 145, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
            iconLabel.setBounds(2, 2, 145, 145);

            iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            iconLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    detail_id = pillId;
                    OpenModal(ModalName.DETAIL);
                }
            });

            card.add(iconLabel);
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

    private void updateCountLabel(Integer pillId) {
        int updated = new PillDAO().getPillAmount(pillId);
        countLabelMap.get(pillId).setText("남은 수량: " + updated);
    }

    private void OpenModal(ModalName modalName) {
        JDialog dialog = null;

        if (modalName == ModalName.DETAIL) {
            dialog = new PillDetailDialog(this);
        } else if (modalName == ModalName.ADD) {
            dialog = new PillAddDialog(this);
        } else if (modalName == ModalName.TIMESETTING) {
            dialog = new PillTimeSettingDialog(this);
        }

        if (dialog != null) {
            dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
    }

    public Integer getDetailId() {
        return detail_id;
    }

    public void update() {
        PillDAO pillDAO = new PillDAO();
        pillDAO.releaseData();
        pillDAO.loadDBData();
        new PillYnDAO().insertInitialYNData();

        if (centerPanel != null && centerPanel.getComponentCount() != 0) {
            centerPanel.removeAll();
            centerPanel.add(createGrid());
            centerPanel.revalidate();
            centerPanel.repaint();
        }
    }

    private JPanel createGrid() {
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(CommonStyle.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        HashMap<Integer, PillDTO> pillsMap = PillManager.getInst().getPillsMap();
        int col = 0, row = 0;
        for (Integer id : pillsMap.keySet()) {
            gbc.gridx = col;
            gbc.gridy = row;
            gridPanel.add(createPillCard(id), gbc);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        return gridPanel;
    }
}
