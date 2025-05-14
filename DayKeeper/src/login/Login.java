package login;

import javax.swing.*;

import config.BaseFrame;

import java.awt.*;
import java.awt.event.*;

/*
 * 생성자 : 문원주
 * 생성일 : 25.05.17
 * 파일명 : Login.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 로그인 기본 프레임 설정
 */

public class Login extends JPanel {
    private JTextField usernameField; // 사용자 이름 입력 필드
    private JPasswordField passwordField; // 비밀번호 입력 필드
    private JLabel messageLabel; // 오류 메시지 출력용 라벨

    public Login() {
        setLayout(new GridBagLayout()); // 레이아웃 설정
        setBackground(Color.WHITE); // 배경 흰색

        GridBagConstraints gbc = new GridBagConstraints(); // 위치 제약 설정
        gbc.insets = new Insets(8, 10, 8, 10); // 여백 설정
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 상단 제목: DAY-KEEPER
        JLabel header = new JLabel("DAY-KEEPER", SwingConstants.CENTER); // 프로젝트 제목
        header.setFont(new Font("SansSerif", Font.BOLD, 40)); // 제목 폰트 크기 증가
        header.setForeground(new Color(30, 100, 180)); // 파란색 계열
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(header, gbc); // 패널에 추가

        // USER NAME 라벨
        gbc.gridwidth = 1; // 열 너비 설정
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        add(new JLabel("USER NAME"), gbc); // 라벨 추가

        // 사용자 이름 입력 필드
        gbc.gridx = 1; // 두 번째 열
        usernameField = new JTextField(); // 입력 필드
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); // 밑줄 스타일
        add(usernameField, gbc);

        // PASSWORD 라벨
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        add(new JLabel("PASSWORD"), gbc); // 라벨 추가

        // 비밀번호 입력 필드
        gbc.gridx = 1; // 두 번째 열
        passwordField = new JPasswordField(); // 비밀번호 필드
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); // 밑줄 스타일
        add(passwordField, gbc);

        // 로그인 버튼
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        gbc.gridwidth = 2; // 두 열에 걸쳐 버튼 배치
        JButton loginButton = new JButton("LOGIN"); // 로그인 버튼 생성
        loginButton.setBackground(new Color(30, 100, 180)); // 파란 배경
        loginButton.setForeground(Color.WHITE); // 흰 글자
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14)); // 글꼴 설정
        loginButton.setFocusPainted(false); // 포커스 테두리 제거
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 커서 모양 손
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(30, 100, 180), 10, true)); // 둥근 테두리
        loginButton.setOpaque(true); // 불투명 설정
        loginButton.addActionListener(e -> attemptLogin()); // 클릭 이벤트
        add(loginButton, gbc);

        // 메시지 출력 라벨
        gbc.gridy++; // 위치 변경
        messageLabel = new JLabel("", SwingConstants.CENTER); // 메시지용 라벨
        messageLabel.setForeground(Color.RED); // 빨간 텍스트
        add(messageLabel, gbc);

        // 하단 링크 패널
        gbc.gridy++; // 위치 변경
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 링크 패널
        linkPanel.setBackground(Color.WHITE); // 배경 흰색

        JLabel signupLabel = new JLabel("회원가입"); // 회원가입
        signupLabel.setForeground(Color.BLUE.darker()); // 파란색
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 클릭 가능 커서

        // 회원가입 라벨을 클릭하면 현재 로그인 패널이 속한 최상위 프레임을 부모로 하여
        // JoinDialog(회원가입 창)를 모달로 띄움
        signupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // new JoinDialog((JFrame) SwingUtilities.getWindowAncestor(Login.this));
            }
        });

        JLabel forgotLabel = new JLabel("아이디&비밀번호 찾기"); // 아이디&비밀번호 찾기
        forgotLabel.setForeground(Color.BLUE.darker()); // 파란색
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 클릭 가능 커서

        linkPanel.add(signupLabel); // 링크 추가
        linkPanel.add(forgotLabel); // 링크 추가
        add(linkPanel, gbc); // 패널에 링크 추가
    }

    // 로그인 시도 메서드
    private void attemptLogin() {
        String id = usernameField.getText(); // 아이디 입력값
        String pw = new String(passwordField.getPassword()); // 비밀번호 입력값

        // 향후 DB 연동 예정
        if (id.equals("user") && pw.equals("1234")) { // 임시 로그인 성공 조건
            messageLabel.setText(""); // 메시지 제거
            JOptionPane.showMessageDialog(this, "로그인 성공!"); // 알림창
            // TODO: 통계 페이지 전환 연결
        } else {
            messageLabel.setText("아이디 또는 비밀번호가 잘못되었습니다."); // 오류 메시지
        }
    }

    // 테스트용 메인 메서드
    public static void main(String[] args) {
        BaseFrame frame = new BaseFrame(); // 기본 프레임 생성
        frame.setContentPane(new Login()); // Login 패널 삽입
        frame.setVisible(true); // 화면에 표시
    }

}
