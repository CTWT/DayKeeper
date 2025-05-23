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

import dbConnection.UserDAO;
import dbConnection.UserDTO;

/*
 * 생성자 : 이주하
 * 생성일 : 25.05.16
 * 파일명 : PwFInd.java
 * 수정자 : 
 * 수정일 : 
 * 설명 : 비밀번호 찾기 패널 설정
 */

public class PwFind extends JPanel {

    private JTextField idField; // 아이디 입력 필드
    private JTextField nameField; // 이름 입력 필드
    private JLabel resultLabel; // 비밀번호 결과 또는 오류 메시지 출력 라벨
    private CardLayout cardLayout; // 입력 화면, 결과 화면 전환용 카드 레이아웃
    private JPanel cardPanel; // 카드 레이아웃이 적용된 패널

    /**
     * 생성자: PwFind 패널을 초기화,
     * 사용자 입력 화면과 결과 화면 구성
     * 
     * 입력 화면: 아이디와 이름 입력 필드, '찾기' 버튼으로 구성,
     * 결과 화면: 조회 결과 메시지와 '닫기' 버튼으로 구성.
     */

    public PwFind() {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 입력 화면 구성
        // 아이디와 이름을 입력받는 필드 생성 및 레이아웃 설정
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.WHITE);

        // 아이디 입력 필드 생성
        idField = new JTextField(15);
        JPanel idWrapper = LabeledField("아이디", idField);

        // 이름 입력 필드 생성
        nameField = new JTextField(15);
        JPanel nameWrapper = LabeledField("이름", nameField);

        // 찾기 버튼 및 클릭 설정
        // 비밀번호 찾기
        JPanel inputButtonPanel = ButtonPanel("찾기", e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            if (!id.isEmpty() && !name.isEmpty()) {
                UserDTO user = new UserDTO();
                user.setId(id);
                user.setName(name);
                UserDAO dao = new UserDAO();
                String foundPw = dao.findPwByIdAndName(user);
                if (foundPw != null) {
                    resultLabel.setText("비밀번호: " + foundPw);
                } else {
                    resultLabel.setText("일치하는 정보가 없습니다.");
                }
                cardLayout.show(cardPanel, "RESULT");
            }
        });

        // 입력 필드 및 버튼 배치
        inputPanel.add(Box.createVerticalStrut(20));
        inputPanel.add(idWrapper);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(nameWrapper);
        inputPanel.add(Box.createVerticalStrut(20));
        inputPanel.add(inputButtonPanel);

        // 결과 화면 구성
        // 비밀번호 결과 또는 오류 메시지, 닫기 버튼 포함
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);

        // 결과 메시지 라벨 설정
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        resultLabel.setForeground(new Color(30, 100, 180));

        JPanel resultButtonPanel = ButtonPanel("닫기", e -> {
            Container parent = getParent();
            while (parent != null && !(parent instanceof JPanel)) {
                parent = parent.getParent();
            }
            if (parent != null) {
                CardLayout layout = (CardLayout) parent.getLayout();
                layout.show(parent, "MENU");
            }
        });

        // 결과 라벨과 버튼 패널 결과 화면에 배치
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        resultPanel.add(resultButtonPanel, BorderLayout.SOUTH);

        // 카드 패널에 입력 화면, 결과 화면 추가
        cardPanel.add(inputPanel, "INPUT");
        cardPanel.add(resultPanel, "RESULT");

        // 메인 패널에 카드 패널 추가
        add(cardPanel, BorderLayout.CENTER);

    }

    /**
     * 입력 필드와 결과 라벨을 초기화,
     * 다시 입력 화면("INPUT")으로 전환
     */
    public void reset() {
        idField.setText("");
        nameField.setText("");
        resultLabel.setText("");
        cardLayout.show(cardPanel, "INPUT");
    }

    /**
     * @param labelText 라벨에 표시할 텍스트
     * @param field     입력 받을 JTextField 컴포넌트
     * @return 라벨과 입력 필드를 포함하는 JPanel
     */
    public static JPanel LabeledField(String labelText, JTextField field) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        wrapper.add(label);
        wrapper.add(field);
        return wrapper;
    }

    /**
     * @param text     버튼에 표시할 텍스트
     * @param listener 버튼 클릭 시 동작할 ActionListener
     * @return 버튼이 포함된 JPanel
     */
    public static JPanel ButtonPanel(String text, java.awt.event.ActionListener listener) {
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

}
