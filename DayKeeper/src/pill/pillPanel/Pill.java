package pill.pillPanel;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;
import config.BaseFrame;
import config.ScreenType;
import pill.pillDAO.PillDAO;
import pill.pillDAO.PillYnDAO;
import pill.pillManager.PillDTO;
import pill.pillManager.PillManager;
import pill.pillManager.ResourcesManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.HashMap;
import java.util.Map;


/*
 * 작성자 : 임해균
 * 작성일 : 2025.05.16
 * 수정자 : 김관호
 * 작성일 : 2025.05.16
 * 파일명 : PillListPanel.java
 * 설명 : 전체 영양제 목록을 카드 형식으로 보여주는 패널
 *       - 클릭 선택 기능 제거
 *       - 이미지 클릭 시 상세 보기 전환
 *       - '영양제 섭취' 버튼 누르면 전체 약 수량 -1 처리
 */

 enum ModalName{
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

        // 상단 제목
        JLabel title = CommonStyle.createTitleLabel();
        title.setText("등록된 영양제");
        add(title, BorderLayout.NORTH);

        // 스크롤 가능 중앙 패널
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
        BottomPanelComponents bottom = createBottomPanel();

        add(bottom.panel, BorderLayout.SOUTH);
    }

    /**
     * 해당 id의 영양제에 대한 패널 리턴
     *
     * @param pillId 영양제 id
     * @return 영양제 정보 패널
     */
    private JPanel createPillCard(Integer pillId) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(CommonStyle.BACKGROUND_COLOR);
        wrapper.setPreferredSize(new Dimension(160, 200));

        String pillName = PillManager.getInst().getDataById(pillId).getPillName();
        int amount = new PillDAO().getPillAmount(pillId);

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

    /**
     * 해당 id의 영양제의 개수를 감소시킵니다.
     *
     * @param pillId 영양제 id
     */
    private void updateCountLabel(Integer pillId) {
        int updated = new PillDAO().getPillAmount(pillId);
        countLabelMap.get(pillId).setText("남은 수량: " + updated);
    }

    private void OpenModal(ModalName modalName) {
        JDialog dialog = null;
        
        if(modalName == ModalName.DETAIL){
            dialog = new PillDetailDialog(this);
        }
        
        else if(modalName == ModalName.ADD) {
            dialog = new PillAddDialog(this);
        }
        
        else if(modalName == ModalName.TIMESETTING){
            dialog = new PillTimeSettingDialog(this);
        }
        
        if(dialog != null){
            dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
            dialog.setLocationRelativeTo(null); // 화면 중앙에 위치
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true); 
        }
    }

    public Integer getDetailId(){
        return detail_id;
    }

    public void update(){
        PillDAO pillDAO = new PillDAO();
        pillDAO.releaseData();
        pillDAO.loadDBData();
        new PillYnDAO().insertInitialYNData();
        
        if(centerPanel != null && centerPanel.getComponentCount() != 0){
            centerPanel.removeAll();
            centerPanel.add(createGrid());
            centerPanel.revalidate();
            centerPanel.repaint();
        }
        
    }

    private JPanel createGrid(){
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

        return gridPanel;
    }

    private BottomPanelComponents createBottomPanel()
    {
        BottomPanelComponents comp = new BottomPanelComponents();

        comp.pillAdd = new JButton("➕ 추가");
        comp.pillReturnHome = new JButton("🏠 처음으로");
        comp.pillTimeSetting = new JButton("⏱ 시간 설정");
        comp.pillConsume = new JButton("💊 영양제 섭취");

        CommonStyle.stylePrimaryButton(comp.pillAdd);
        CommonStyle.stylePrimaryButton(comp.pillReturnHome);
        CommonStyle.stylePrimaryButton(comp.pillTimeSetting);
        CommonStyle.stylePrimaryButton(comp.pillConsume);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.add(comp.pillAdd);
        bottomPanel.add(comp.pillReturnHome);
        bottomPanel.add(comp.pillTimeSetting);
        bottomPanel.add(comp.pillConsume);

        comp.panel = bottomPanel;

        comp.pillAdd.addActionListener(e -> OpenModal(ModalName.ADD));
        comp.pillReturnHome.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.TODOLIST);
        });
        comp.pillTimeSetting.addActionListener(e -> OpenModal(ModalName.TIMESETTING));

        // 영양제를 이미 섭취했으면 메세지 띄우고 아니라면 영양제 섭취
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
