package pill;

import javax.swing.*;
import dbConnection.DBManager;
import common.CommonStyle;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.*;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : 임해균
 * 작성일 : 2025.05.16
 * 파일명 : PillListPanel.java
 * 설명 : 전체 영양제 목록을 카드 형식으로 보여주는 패널
 *       - 클릭 선택 기능 제거
 *       - 이미지 클릭 시 상세 보기 전환
 *       - '영양제 섭취' 버튼 누르면 전체 약 수량 -1 처리
 */

public class PillListPanel extends JPanel {
    private PillApp app;
    private Map<Integer, JLabel> countLabelMap = new HashMap<>();

    public PillListPanel(PillApp app) {
        this.app = app;

        PillManager.getInst().releaseData();
        PillManager.getInst().loadDBData();

        setLayout(new BorderLayout());
        setBackground(CommonStyle.BACKGROUND_COLOR);

        // 상단 제목
        JLabel title = CommonStyle.createTitleLabel();
        title.setText("등록된 영양제");
        add(title, BorderLayout.NORTH);

        // 카드 그리드 패널
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

        // 스크롤 가능 중앙 패널
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        centerPanel.setBackground(CommonStyle.BACKGROUND_COLOR);
        centerPanel.add(gridPanel);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(40);
        add(scrollPane, BorderLayout.CENTER);

        // 하단 버튼
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

        addBtn.addActionListener(e -> app.showPanel("add"));
        homeBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "처음으로 돌아갑니다."));
        timeBtn.addActionListener(e -> app.showPanel("time"));

        consumeBtn.addActionListener(e -> {
            for (Integer id : pillsMap.keySet()) {
                consumePill(id, 1);
                updateCountLabel(id);
            }
            JOptionPane.showMessageDialog(this, "전체 영양제를 1개씩 섭취 처리했습니다.");
            app.showPanel("list");
        });

        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel createPillCard(Integer pillId) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(CommonStyle.BACKGROUND_COLOR);
        wrapper.setPreferredSize(new Dimension(160, 200));

        String pillName = PillManager.getInst().getDataById(pillId).getPillName();
        int amount = getPillAmount(pillId);

        // 약 이름 + 수량
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

        // 이미지 카드
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
                    app.setDetailId(pillId);
                    app.showPanel("detail");
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
        int updated = getPillAmount(pillId);
        countLabelMap.get(pillId).setText("남은 수량: " + updated);
    }

    public void consumePill(Integer pillId, Integer amount) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "UPDATE pill SET pillAmount = pillAmount - ? WHERE pill_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, amount);
            pstmt.setInt(2, pillId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getPillAmount(Integer pillId) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT pillAmount FROM pill WHERE pill_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, pillId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("pillAmount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
