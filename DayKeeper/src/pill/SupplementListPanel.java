package pill;

import javax.swing.*;
import dbConnection.DBManager;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : ChatGPT
 * 수정일 : 2025.05.16
 * 파일명 : SupplementListPanel.java
 * 설명 : 영양제 목록 패널. 카드 UI 및 하단 버튼 구성. 시간 설정 버튼 추가.
 */

public class SupplementListPanel extends JPanel {
    private SupApp app;

    public SupplementListPanel(SupApp app) {
        this.app = app;

        // DB에서 데이터 불러오기
        PillManager.getInst().loadDBData();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // [상단 제목]
        JLabel title = new JLabel("등록된 영양제", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // [카드 그리드 영역]
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

        // [스크롤 가능한 중앙 패널]
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(gridPanel);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        add(scrollPane, BorderLayout.CENTER);

        // [하단 버튼 영역]
        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(245, 245, 245));

        JButton addBtn = new JButton("➕ 추가");
        JButton homeBtn = new JButton("🏠 처음으로");
        JButton timeBtn = new JButton("⏱ 시간 설정"); // ✅ 추가된 버튼

        Font btnFont = new Font("맑은 고딕", Font.PLAIN, 13);
        addBtn.setFont(btnFont);
        homeBtn.setFont(btnFont);
        timeBtn.setFont(btnFont);

        addBtn.addActionListener(e -> app.showPanel("add"));
        homeBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "처음으로 돌아갑니다."));
        timeBtn.addActionListener(e -> app.showPanel("time")); // ⚠️ 다음 단계에서 구현할 패널

        bottom.add(addBtn);
        bottom.add(homeBtn);
        bottom.add(timeBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * 영양제 카드 하나 생성
     */
    private JPanel createPillCard(Integer pillId) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(new Color(245, 245, 245));
        wrapper.setPreferredSize(new Dimension(160, 200));

        String pillName = PillManager.getInst().getDataById(pillId).getPillName();
        int amount = getPillAmount(pillId);
        int[] count = {amount};

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
                checkBox.setSelected(true);
                checkBox.setEnabled(false);
                consumePill(pillId, 1);
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

    /**
     * DB에 수량 차감 반영
     */
    public void consumePill(Integer pillId, Integer amount) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "UPDATE pill SET pillAmount = pillAmount - ? WHERE pill_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, amount);
            pstmt.setInt(2, pillId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 현재 수량 조회
     */
    public Integer getPillAmount(Integer pillId) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT pillAmount FROM pill WHERE pill_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, pillId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("pillAmount");
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
