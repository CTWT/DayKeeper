package pill;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * 수업명 : Project DayKeeper 
 * 이름 : 임해균 
 * 작성자 : 임해균 
 * 수정자 : 
 * 작성일 : 25.05.14 
 * 파일명 : SupApp.java 
 */

// 메인 프레임 클래스
public class SupApp extends JFrame {
    private CardLayout cardLayout = new CardLayout(); // 화면 전환용 레이아웃
    private JPanel mainPanel = new JPanel(cardLayout); // 화면을 담을 메인 패널

    private SupplementDetailPanel detailPanel; // 상세화면 클래스

    public SupApp() {
        setTitle("daykeeper"); // 프레임 제목 설정
        setSize(500, 600); // 프레임 크기 설정
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 닫기 버튼 누르면 종료
        setLocationRelativeTo(null); // 화면 중앙에 배치

        // 첫 화면, 상세 화면 생성
        SupplementImagePanel imagePanel = new SupplementImagePanel();
        detailPanel = new SupplementDetailPanel();

        // 화면 등록
        mainPanel.add(imagePanel, "image");
        mainPanel.add(detailPanel, "detail");

        add(mainPanel); // 프레임에 메인패널 부착

        // 버튼 클릭 시 화면 전환 이벤트 설정
        imagePanel.addImageClickListener(e -> cardLayout.show(mainPanel, "detail"));
        detailPanel.addBackButtonListener(e -> cardLayout.show(mainPanel, "image"));

        cardLayout.show(mainPanel, "image"); // 첫 화면을 보여줌
        setVisible(true); // 화면 표시
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SupApp::new); // GUI는 EDT에서 실행
    }
}

// 첫 화면 (영양제 이미지 버튼만 있음)
class SupplementImagePanel extends JPanel {
    private JButton imageButton = new JButton("영양제 이미지");

    public SupplementImagePanel() {
        setLayout(new BorderLayout());

        // 상단 제목
        JLabel title = new JLabel("daykeeper", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // 중앙에 큰 버튼
        imageButton.setPreferredSize(new Dimension(150, 150));
        imageButton.setBackground(Color.ORANGE);
        add(imageButton, BorderLayout.CENTER);
    }

    // 버튼 클릭 이벤트 등록 메서드
    public void addImageClickListener(ActionListener listener) {
        imageButton.addActionListener(listener);
    }
}

// 상세화면 클래스 (사진 여러 개 2열, 이름 위에 표시)
class SupplementDetailPanel extends JPanel {
    private JButton addButton = new JButton("영양제 추가하기");
    private JButton backButton = new JButton("뒤로가기");

    public SupplementDetailPanel() {
        setLayout(new BorderLayout());

        // 상단 제목
        JLabel titleLabel = new JLabel("daykeeper", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // 중앙 컨텐츠 영역
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 목록 타이틀
        JLabel nameLabel = new JLabel("영양제 목록");
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        centerPanel.add(nameLabel, BorderLayout.NORTH);

        // 2열 그리드로 사진 카드 나열할 패널
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // 예시 영양제 10개 추가
        for (int i = 1; i <= 10; i++) {
            gridPanel.add(createSupplementCard("영양제" + i));
        }

        // 스크롤 패널로 감싸기 (세로 스크롤만)
        JScrollPane scrollPane = new JScrollPane(gridPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setPreferredSize(new Dimension(440, 320));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // 중앙 패널 추가
        add(centerPanel, BorderLayout.CENTER);

        // 하단 버튼 영역
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 카드 형식의 영양제 UI 생성
    private JPanel createSupplementCard(String name) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // 카드 테두리

        // 상단: 이름 표시
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

        // 중앙: 사진 영역
        JLabel photoBox = new JLabel("사진", SwingConstants.CENTER);
        photoBox.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        photoBox.setOpaque(true);
        photoBox.setBackground(Color.LIGHT_GRAY);
        photoBox.setPreferredSize(new Dimension(100, 100));

        cardPanel.add(nameLabel, BorderLayout.NORTH);
        cardPanel.add(photoBox, BorderLayout.CENTER);

        return cardPanel;
    }

    // 뒤로가기 버튼에 이벤트 연결
    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
