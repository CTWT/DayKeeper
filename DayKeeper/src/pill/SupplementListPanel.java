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
 * 작성일 : 2025.05.14
 * 파일명 : SupplementListPanel.java
 */

// 영양제 목록을 그리드 형태로 보여주는 UI 패널
public class SupplementListPanel extends JPanel {
    private SupApp app;

    public SupplementListPanel(SupApp app) {
        this.app = app;

        PillManager.getInst().LoadDBData();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // 배경 연회색

        // 상단 제목 라벨
        JLabel title = new JLabel("등록된 영양제", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // 카드들을 담을 그리드 패널
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 4, 15, 15)); // 4열, 간격 추가
        gridPanel.setBackground(new Color(245, 245, 245));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 예시 영양제 항목 생성
        HashMap<Integer, PillDTO> pillsMap = PillManager.getInst().getPillsMap();
        Iterator<Integer> iterator = pillsMap.keySet().iterator();
        while(iterator.hasNext()){
            Integer id = iterator.next();
            gridPanel.add(createPillCard(id));
        }

        // 스크롤 패널로 감싸기
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(245, 245, 245));
        JButton addBtn = new JButton("➕ 추가");
        JButton homeBtn = new JButton("🏠 처음으로");

        // 한글 깨짐 방지를 위한 폰트 지정
        Font buttonFont = new Font("맑은 고딕", Font.PLAIN, 13);
        addBtn.setFont(buttonFont);
        homeBtn.setFont(buttonFont);

        addBtn.addActionListener(e -> app.showPanel("add"));
        homeBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "처음으로 돌아갑니다."));

        bottom.add(addBtn);
        bottom.add(homeBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    // 개별 영양제 카드 생성
    private JPanel createPillCard(Integer pillId) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(new Color(245, 245, 245));
        wrapper.setMaximumSize(new Dimension(150, 180)); // 적절한 고정 크기

        // 라벨: 카드 상단 제목처럼
        String pillName = PillManager.getInst().getDatabyId(pillId).getPillName();
        JLabel nameLabel = new JLabel(pillName);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // 카드 본체
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(122, 122));
        card.setMaximumSize(new Dimension(122, 122));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        try {
            // 이미지 불러오기
            Image image = ResourcesManager.getInst().getImagebyName(pillName);
            // 크기 조정
            Image scaledImage = image.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            // 다시 ImageIcon으로 감싸기
            JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(iconLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            System.out.println("이미지 로드 오류: " + e.getMessage());
            JLabel errorLabel = new JLabel("이미지 없음", SwingConstants.CENTER);
            card.add(errorLabel, BorderLayout.CENTER);
        }

        wrapper.add(nameLabel, BorderLayout.NORTH);
        wrapper.add(card, BorderLayout.CENTER);

        return wrapper;
    }
}
