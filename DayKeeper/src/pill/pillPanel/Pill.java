// package & import 동일 생략
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
import dbConnection.PillAlramDAO;
import dbConnection.PillDAO;
import dbConnection.PillDTO;
import dbConnection.PillYnDAO;
import pill.pillManager.PillManager;

public class Pill extends JPanel {
    private Map<Integer, JLabel> countLabelMap = new HashMap<>();
    private Integer detail_id;
    private JLabel timeInfoLabel;
    private JPanel centerPanel;
    private JScrollPane scrollPane;
    private boolean isHorizontal = false;

    public Pill() {
        setLayout(new BorderLayout());
        setBackground(CommonStyle.BACKGROUND_COLOR);

        // 상단 타이틀
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        JLabel titleLabel = CommonStyle.createTitleLabel();
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel subTitleWrapper = new JPanel(new BorderLayout());
        subTitleWrapper.setBackground(CommonStyle.BACKGROUND_COLOR);
        subTitleWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // "등록된 영양제" 라벨
        JLabel subTitleLabel = new JLabel("💊 등록된 영양제");
        subTitleLabel.setFont(CommonStyle.BUTTON_FONT);
        subTitleLabel.setForeground(CommonStyle.PRIMARY_COLOR);
        subTitleLabel.setOpaque(false);
        subTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // 🌓 토글 버튼
        JButton toggleButton = new JButton("🌓");
        toggleButton.setPreferredSize(new Dimension(40, 30));
        toggleButton.setFocusPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setBorder(null);
        toggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleButton.setToolTipText("가로/세로 보기 전환");
        toggleButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        toggleButton.addActionListener(e -> toggleLayout());

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        togglePanel.setBackground(CommonStyle.BACKGROUND_COLOR);
        togglePanel.add(toggleButton);

        JPanel labelWithToggle = new JPanel(new BorderLayout());
        labelWithToggle.setBackground(CommonStyle.BACKGROUND_COLOR);
        labelWithToggle.add(subTitleLabel, BorderLayout.WEST);
        labelWithToggle.add(togglePanel, BorderLayout.EAST);

        // 알람 라벨
        timeInfoLabel = new JLabel("⏰ 알람 시간 : " + getFormattedAlarmTime());
        timeInfoLabel.setFont(CommonStyle.BUTTON_FONT);
        timeInfoLabel.setForeground(CommonStyle.PRIMARY_COLOR);
        timeInfoLabel.setOpaque(false);
        timeInfoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        subTitleWrapper.add(labelWithToggle, BorderLayout.WEST);
        subTitleWrapper.add(timeInfoLabel, BorderLayout.EAST);

        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(subTitleWrapper);
        add(titlePanel, BorderLayout.NORTH);

        // 센터
        centerPanel = new JPanel();
        centerPanel.setBackground(CommonStyle.BACKGROUND_COLOR);

        scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(40);

        scrollPane.addMouseWheelListener(e -> {
            if (isHorizontal && e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                JScrollBar hBar = scrollPane.getHorizontalScrollBar();
                int move = e.getUnitsToScroll() * hBar.getUnitIncrement();
                hBar.setValue(hBar.getValue() + move);
                e.consume();
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        update();

        BottomPanelComponents bottomComponents = createBottomPanel();
        add(bottomComponents.panel, BorderLayout.SOUTH);
    }

    private void toggleLayout() {
        isHorizontal = !isHorizontal;
        update();
    }

    public void update() {
        PillDAO pillDAO = new PillDAO();
        pillDAO.releaseData();
        pillDAO.loadDBData();
        new PillYnDAO().insertInitialYNData();

        if (centerPanel != null) {
            centerPanel.removeAll();

            if (isHorizontal) {
                centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            } else {
                centerPanel.setLayout(new GridBagLayout());
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            }

            HashMap<Integer, PillDTO> pillsMap = PillManager.getInst().getPillsMap();
            int col = 0, row = 0;
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.NORTHWEST;

            for (Integer id : pillsMap.keySet()) {
                JPanel card = createPillCard(id);
                if (isHorizontal) {
                    centerPanel.add(card);
                } else {
                    gbc.gridx = col;
                    gbc.gridy = row;
                    centerPanel.add(card, gbc);
                    col++;
                    if (col == 4) {
                        col = 0;
                        row++;
                    }
                }
            }

            centerPanel.revalidate();
            centerPanel.repaint();
        }
    }

    private JPanel createPillCard(Integer pillId) {
        boolean isWide = isHorizontal;

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(CommonStyle.BACKGROUND_COLOR);

        int wrapperWidth = isWide ? 350 : 160;
        int wrapperHeight = isWide ? 300 : 200;
        wrapper.setPreferredSize(new Dimension(wrapperWidth, wrapperHeight));

        String pillName = PillManager.getInst().getDataById(pillId).getPillName();
        int amount = new PillDAO().getPillAmount(pillId);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(CommonStyle.BACKGROUND_COLOR);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel nameLabel = CommonStyle.createLabel(pillName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, isWide ? 22 : 16));
        nameLabel.setForeground(new Color(40, 40, 40));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ✅ 수량 텍스트 개선 (이모지 + 숫자 강조)
        JLabel countLabel = new JLabel("<html>💊 수량: <b><span style='color:#2d74da;'>"
                + amount + "</span></b></html>");
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, isWide ? 17 : 14));
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        countLabelMap.put(pillId, countLabel);

        labelPanel.add(nameLabel);
        labelPanel.add(Box.createVerticalStrut(5));
        labelPanel.add(countLabel);

        JPanel card = new RoundedGradientPanel();
        card.setLayout(null);
        int cardW = isWide ? 330 : 150;
        int cardH = isWide ? 220 : 150;
        card.setPreferredSize(new Dimension(cardW, cardH));

        try {
            String url = "pill/" + pillName;
            JLabel iconLabel = ImgConfig.imgLabelComponent(url, cardW, cardH);
            iconLabel.setBounds(0, 0, cardW, cardH);
            iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            iconLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    detail_id = pillId;
                    OpenModal("DETAIL");
                }
            });
            card.add(iconLabel);
        } catch (Exception e) {
            JLabel errorLabel = CommonStyle.createLabel("이미지 없음");
            errorLabel.setBounds(10, cardH / 2 - 15, 130, 30);
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
        countLabelMap.get(pillId).setText(
                "<html>💊 수량: <b><span style='color:#2d74da;'>" + updated + "</span></b></html>");
    }

    private void OpenModal(String modalName) {
        JDialog dialog = null;
        if (modalName.equals("DETAIL")) {
            dialog = new PillDetailDialog(this);
        } else if (modalName.equals("ADD")) {
            dialog = new PillAddDialog(this);
        } else if (modalName.equals("TIMESETTING")) {
            dialog = new PillTimeSettingDialog(this, timeInfoLabel);
        }

        if (dialog != null) {
            dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
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

    private BottomPanelComponents createBottomPanel() {
        BottomPanelComponents comp = CommonStyle.createBottomPanel();

        comp.pillAdd.setVisible(true);
        comp.returnHome.setVisible(true);
        comp.pillTimeSetting.setVisible(true);
        comp.pillConsume.setVisible(true);

        comp.pillAdd.addActionListener(e -> OpenModal("ADD"));
        comp.returnHome.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.TODOLIST);
        });
        comp.pillTimeSetting.addActionListener(e -> OpenModal("TIMESETTING"));
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

    class RoundedGradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(240, 248, 255),
                    getWidth(), getHeight(), new Color(210, 230, 255)
            );

            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
        }
    }

    public Integer getDetailedId() {
        return detail_id;
    }
}
