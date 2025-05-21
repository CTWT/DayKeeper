package login;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import common.Session;
import dbConnection.UserDAO;

/*
 * 생성자 : 이주하
 * 생성일 : 25.05.16
 * 파일명 : IdFInd.java
 * 수정자 : 
 * 수정일 : 
 * 설명 : 아이디 찾기 패널 설정
 */

public class IdFind extends JPanel {
    private JTextField nameField; // 이름 입력 필드
    private JLabel resultLabel; // 결과를 표시하는 라벨
    private CardLayout cardLayout; // 카드 레이아웃(입력/결과 전환)
    private JPanel cardPanel; // 카드 패널(입력/결과 패널이 들어감)

    /**
     * 아이디 찾기 패널 생성자
     * - 입력 패널과 결과 패널을 CardLayout으로 전환
     */
    public IdFind() {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 입력 패널, 결과 패널 생성
        JPanel inputPanel = buildInputPanel();
        JPanel resultPanel = buildResultPanel();

        // 카드 패널에 입력,결과 패널 추가
        cardPanel.add(inputPanel, "INPUT");
        cardPanel.add(resultPanel, "RESULT");

        add(cardPanel, BorderLayout.CENTER);

    }

    /**
     * 이름 입력 UI 패널 생성
     * 
     * @return 입력 패널
     */
    private JPanel buildInputPanel() {
        // 수직 레이아웃 패널 생성
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.WHITE);

        // 이름 입력 필드 및 라벨 생성
        nameField = new JTextField(15);
        JPanel nameWrapper = LabeledField("이름", nameField);

        // "찾기" 버튼 및 이벤트 연결
        JPanel inputButtonPanel = ButtonPanel("찾기", e -> handleFind());

        inputPanel.add(Box.createVerticalStrut(30));
        inputPanel.add(nameWrapper);
        inputPanel.add(Box.createVerticalStrut(20));
        inputPanel.add(inputButtonPanel);

        return inputPanel;
    }

    /**
     * 결과를 보여주는 UI 패널 생성
     * 
     * @return 결과 패널
     */
    private JPanel buildResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);
        // 결과 표시 라벨 생성
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        resultLabel.setForeground(new Color(30, 100, 180));

        // "닫기" 버튼 생성 및 이벤트 연결
        JPanel resultButtonPanel = ButtonPanel("닫기", e -> handleClose());

        // 패널에 결과 라벨과 버튼 추가
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        resultPanel.add(resultButtonPanel, BorderLayout.SOUTH);

        return resultPanel;
    }

    /**
     * "찾기" 버튼 클릭 시 이벤트 처리
     * - 이름으로 아이디를 검색하여 결과를 표시
     */
    private void handleFind() {
        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            UserDAO dao = new UserDAO();
            String foundId = dao.findIdByName(name); // 이름으로 아이디 조회
            if (foundId != null) {
                // 아이디를 찾은 경우 결과 표시 및 세션 저장
                resultLabel.setText("아이디: " + foundId);
                Session.setUserId(foundId);
            } else {
                // 아이디를 찾지 못한 경우: 안내 메시지 표시
                resultLabel.setText("해당 이름으로 등록된 아이디가 없습니다.");

            }
            // 결과 화면으로 전환
            cardLayout.show(cardPanel, "RESULT");
        }
    }

    /**
     * "닫기" 버튼 클릭 시 이벤트 처리
     * - 메뉴 패널로 돌아감
     */
    private void handleClose() {
        Container parent = getParent();
        while (parent != null && !(parent instanceof JPanel)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            CardLayout layout = (CardLayout) parent.getLayout();

            // 메뉴 화면으로 전환
            layout.show(parent, "MENU");
        }

    }

    /**
     * 공통 라벨+입력필드 패널 생성
     * 
     * @param labelText 라벨 텍스트
     * @param field     입력 필드
     * @return 패널
     */
    private JPanel LabeledField(String labelText, JTextField field) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        wrapper.add(label);
        wrapper.add(field);
        return wrapper;
    }

    /**
     * 공통 버튼 포함 패널 생성 및 이벤트 리스너 등록
     * 
     * @param text     버튼 텍스트
     * @param listener 버튼 클릭 이벤트
     * @return 패널
     */
    private JPanel ButtonPanel(String text, java.awt.event.ActionListener listener) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(80, 30));
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);
        panel.add(button);
        return panel;
    }

    /**
     * 패널 상태 리셋 (입력화면으로 전환 및 필드 초기화)
     */
    public void reset() {
        nameField.setText("");
        resultLabel.setText("");
        cardLayout.show(cardPanel, "INPUT");
    }
}
