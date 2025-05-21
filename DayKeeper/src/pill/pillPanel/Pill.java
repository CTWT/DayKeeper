package pill.pillPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;
import config.BaseFrame;
import config.ImgConfig;
import config.ScreenType;
import pill.pillDAO.PillDAO;
import pill.pillDAO.PillYnDAO;
import pill.pillDAO.PillAlramDAO;
import pill.pillManager.PillDTO;
import pill.pillManager.PillManager;

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
    private JLabel timeInfoLabel;
    JPanel centerPanel;

    public Pill() {
        setLayout(new BorderLayout());
        setBackground(CommonStyle.BACKGROUND_COLOR);
        update();

        // 타이틀 패널
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        JLabel titleLabel = CommonStyle.createTitleLabel();
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel subTitleWrapper = new JPanel(new BorderLayout());
        subTitleWrapper.setBackground(CommonStyle.BACKGROUND_COLOR);
        subTitleWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel subTitleLabel = CommonStyle.createLabel("등록된 영양제");
        subTitleLabel.setFont(CommonStyle.BUTTON_FONT);

        String alarmTimeText = getFormattedAlarmTime();
        timeInfoLabel = CommonStyle.createLabel("⏰ 알람 시간 : " + alarmTimeText);
        timeInfoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timeInfoLabel.setForeground(CommonStyle.PRIMARY_COLOR);
        timeInfoLabel.setFont(CommonStyle.BUTTON_FONT);

        subTitleWrapper.add(subTitleLabel, BorderLayout.WEST);
        subTitleWrapper.add(timeInfoLabel, BorderLayout.EAST);

        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(subTitleWrapper);
        add(titlePanel, BorderLayout.NORTH);

        // 스크롤 영역
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

        // 하단 버튼
        BottomPanelComponents bottomComponents = createBottomPanel();
        add(bottomComponents.panel, BorderLayout.SOUTH);
    }

    private String getFormattedAlarmTime() {
        String raw = new PillAlramDAO().getRegisteredTime();
        try {
            int time = Integer.parseInt(raw);
            String ampm = (time < 12) ? "오전" : "오후";
            int hour = (time == 0) ? 12 : time % 12;
            return String.format("%s %02d시", ampm, hour);
        } catch (Exception e) {
            return "00시";
        }
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

        JLabel nameLabel = CommonStyle.createLabel(pillName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countLabel = CommonStyle.createLabel("남은 수량: " + amount);
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
            String url = "pill/" + pillName;
            JLabel iconLabel = ImgConfig.imgLabelComponent(url, 145, 145);
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
            JLabel errorLabel = CommonStyle.createLabel("이미지 없음");
            errorLabel.setBounds(10, 60, 130, 30);
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
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
            dialog = new PillTimeSettingDialog(this, timeInfoLabel);
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

    private BottomPanelComponents createBottomPanel() {
        BottomPanelComponents comp = CommonStyle.createBottomPanel();

        comp.pillAdd.setVisible(true);
        comp.pillReturnHome.setVisible(true);
        comp.pillTimeSetting.setVisible(true);
        comp.pillConsume.setVisible(true);

        comp.pillAdd.addActionListener(e -> OpenModal(ModalName.ADD));
        comp.pillReturnHome.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.TODOLIST);
        });
        comp.pillTimeSetting.addActionListener(e -> OpenModal(ModalName.TIMESETTING));

        comp.pillConsume.addActionListener(e -> {
            if (PillManager.getInst().getPillsMap().size() <= 0) {
                JOptionPane.showMessageDialog(this, "먼저 영양제를 추가해주세요.");
                return;
            }

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

        return comp;
    }
}
