package login;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*
 * 생성자 : 이주하
 * 생성일 : 25.05.15
 * 파일명 : Signup.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 회원가입 기본 프레임 설정
 */

public class Signup extends JPanel {

    private JTextField newIdField; // 사용자 이름 입력 필드
    private JPasswordField newPwField; // 비밀번호 입력 필드
    private JTextField usernameField; // 사용자 이름 입력 필드

    public Signup(){
        setLayout(new GridBagLayout()); // 레이아웃 설정
        setBackground(Color.WHITE); // 배경 흰색

        GridBagConstraints gbc = new GridBagConstraints(); // 위치 제약 설정
        gbc.insets = new Insets(8, 10, 8, 10); // 여백 설정
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 상단 제목: Sign Up
        JLabel headLabel = new JLabel("Sign Up" , SwingConstants.CENTER); // 회원가입 제목
        headLabel.setFont(new Font("SansSerif", Font.BOLD, 40)); // 제목 폰트 크기 
        headLabel.setForeground(new Color(30, 100, 180)); // 파란색 계열
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(headLabel, gbc); // 패널에 추가
        

        // User Id 라벨
        gbc.gridwidth = 1; // 열 너비 설정
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        add(new JLabel("ID"), gbc); // 라벨 추가

        // User Id 입력 필드
        gbc.gridx = 1; // 두 번째 열
        newIdField = new JTextField(); // 입력 필드
        newIdField.setPreferredSize(new java.awt.Dimension(200, 20));
        newIdField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); // 밑줄 스타일
        add(newIdField, gbc);

        // User Id 중복확인 버튼
        gbc.gridx = 3; // 세 번째 열
        JButton checkButton = new JButton("중복확인");
        checkButton.setBackground(new Color(30, 100, 180)); // 파란 배경
        checkButton.setForeground(Color.WHITE); // 흰 글자
        checkButton.setFont(new Font("SansSerif", Font.BOLD, 11)); // 글꼴 설정
        checkButton.setFocusPainted(false); // 포커스 테두리 제거
        checkButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 커서 모양 손
        checkButton.setBorder(BorderFactory.createLineBorder(new Color(30, 100, 180), 10, true)); // 둥근 테두리
        checkButton.setOpaque(true); // 불투명 설정
        checkButton.addActionListener(e->{
            String id = newIdField.getText();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "아이디를 입력해주세요.");
                return;
            }

            UserDAO dao = new UserDAO();
            if(dao.isDuplicateId(id)){
                JOptionPane.showMessageDialog(this,"이미 사용 중인 아이디입니다.");
            }else {
                JOptionPane.showMessageDialog(this,"사용 가능한 아이디입니다!");
            }

        });
        add(checkButton,gbc);
        
        // Password 라벨
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        add(new JLabel("PASSWORD"), gbc); // 라벨 추가

        // Password 입력 필드
        gbc.gridx = 1; // 두 번째 열
        newPwField = new JPasswordField(); // 비밀번호 필드
        newPwField.setPreferredSize(new java.awt.Dimension(200, 20));
        newPwField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); // 밑줄 스타일
        add(newPwField, gbc);

        // User name 입력 라벨
        gbc.gridy ++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        add(new JLabel("USER NAME"), gbc); // 라벨 추가

        // User name 입력 필드
        gbc.gridx = 1; // 두 번째 열
        usernameField = new JTextField(); // 비밀번호 필드
        usernameField.setPreferredSize(new java.awt.Dimension(200, 20));
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); // 밑줄 스타일
        add(usernameField, gbc);

        // 뒤로가기 버튼
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        JButton backButton = new JButton("뒤로가기"); // 뒤로가기 버튼 생성
        backButton.setBackground(new Color(30, 100, 180)); // 파란 배경
        backButton.setForeground(Color.WHITE); // 흰 글자
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14)); // 글꼴 설정
        backButton.setFocusPainted(false); // 포커스 테두리 제거
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 커서 모양 손
        backButton.setBorder(BorderFactory.createLineBorder(new Color(30, 100, 180), 10, true)); // 둥근 테두리
        backButton.setOpaque(true); // 불투명 설정
        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new Login());
            frame.revalidate();
            frame.repaint();
        }); // 로그인 화면으로 돌아가기
        add(backButton,gbc);

        // 회원가입 버튼
        gbc.gridx = 1; // 두 번째 열
        JButton singupButton = new JButton("회원가입"); // 회원가입 버튼 생성
        singupButton.setBackground(new Color(30, 100, 180)); // 파란 배경
        singupButton.setForeground(Color.WHITE); // 흰 글자
        singupButton.setFont(new Font("SansSerif", Font.BOLD, 14)); // 글꼴 설정
        singupButton.setFocusPainted(false); // 포커스 테두리 제거
        singupButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 커서 모양 손
        singupButton.setBorder(BorderFactory.createLineBorder(new Color(30, 100, 180), 10, true)); // 둥근 테두리
        singupButton.setOpaque(true); // 불투명 설정
        singupButton.addActionListener(e -> {
            String id = newIdField.getText().trim();
            String pw = new String(newPwField.getPassword()).trim();
            String name = usernameField.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "아이디를 입력해주세요.");
                return;
            }

            if (pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요.");
                return;
            }
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "이름을 입력해주세요.");
                return;
            }

            JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다!");
        }); // 클릭 이벤트
        add(singupButton, gbc);

}

}
