package pill;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import dbConnection.DBManager;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 수정자 : 김관호
 * 수정일 : 2025.05.15
 * 파일명 : SupplementListPanel.java
 * 설명 : 등록된 영양제 목록을 보여주는 패널
 */
public class SupplementListPanel extends JPanel {
    private SupApp app;
    private int[] count = {5};  // 복용 가능 수량

    /**
     * 생성자: 영양제 목록 패널 초기화
     * @param app 부모 컴포넌트 (SupApp)
     */
    public SupplementListPanel(SupApp app) {
        this.app = app;

        // 데이터 로딩
        PillManager.getInst().loadDBData();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // 상단 제목 라벨 추가
        add(createTitleLabel(), BorderLayout.NORTH);

        // 중앙 그리드 패널 추가
        JScrollPane scrollPane = createGridScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        // 하단 버튼 패널 추가
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * 상단 제목 라벨 생성
     * @return 제목 라벨
     */
    private JLabel createTitleLabel() {
        JLabel title = new JLabel("등록된 영양제", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        return title;
    }

    /**
     * 중앙 그리드 스크롤 패널 생성
     * @return 스크롤 패널
     */
    private JScrollPane createGridScrollPane() {
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // 영양제 카드 추가
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

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(gridPanel);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    /**
     * 하단 버튼 패널 생성
     * @return 하단 버튼 패널
     */
    private JPanel createBottomPanel() {
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
        return bottom;
    }

    /**
     * 영양제 카드 생성
     * @param pillId 영양제 ID
     * @return 영양제 카드 패널
     */
    private JPanel createPillCard(Integer pillId) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(new Color(245, 245, 245));
        wrapper.setPreferredSize(new Dimension(160, 200));

        PillDTO pill = PillManager.getInst().getDataById(pillId);
        JLabel nameLabel = new JLabel(pill.getPillName());
        JLabel countLabel = new JLabel("남은 수량: " + count[0]);

        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        countLabel.setFont(new Font("맑은 고딕", Font.BOLD, 13));

        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(new Color(245, 245, 245));
        labelPanel.add(nameLabel, BorderLayout.WEST);
        labelPanel.add(countLabel, BorderLayout.EAST);

        JPanel card = createCardPanel(pill, countLabel);
        wrapper.add(labelPanel);
        wrapper.add(Box.createRigidArea(new Dimension(0, 5)));
        wrapper.add(card);
        return wrapper;
    }

    /**
     * 카드 패널 생성 (이미지 및 체크박스 포함)
     * @param pill 영양제 DTO
     * @param countLabel 수량 라벨
     * @return 카드 패널
     */
    private JPanel createCardPanel(PillDTO pill, JLabel countLabel) {
        JPanel card = new JPanel(null);
        card.setPreferredSize(new Dimension(150, 150));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        JLabel iconLabel = new JLabel(new ImageIcon(ResourcesManager.getInst().getImagebyName(pill.getPillName())));
        iconLabel.setBounds(2, 2, 145, 145);
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                app.setDetailId(pill.getPill_id());
                app.showPanel("detail");
            }
        });

        card.add(iconLabel);
        return card;
    }

    /**
     * 영양제 복용 처리
     * @param amount 복용 수량
     */
    public void consumePill(Integer amount) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "UPDATE pill SET pillAmount = pillAmount - ? WHERE pillId = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, amount);
            pstmt.setInt(2, app.getDetailId());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
