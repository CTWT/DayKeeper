package login;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;

import common.CommonStyle;

import config.BaseFrame;
import config.ScreenType;
import login.Login.UserSearch;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * 생성자 : 문원주
 * 생성일 : 25.05.15
 * 파일명 : Login.java
 * 수정자 : 이주하
 * 수정일 : 25.05.15
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
        JLabel header = CommonStyle.createTitleLabel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(header, gbc); // 패널에 추가

        // USER NAME 라벨
        gbc.gridwidth = 1; // 열 너비 설정
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        add(CommonStyle.createLabel("USER NAME"), gbc); // 라벨 추가

        // 사용자 이름 입력 필드
        gbc.gridx = 1; // 두 번째 열
        usernameField = new JTextField(); // 입력 필드
        CommonStyle.underline(usernameField); // 밑줄 스타일
        add(usernameField, gbc);

        // PASSWORD 라벨
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        add(CommonStyle.createLabel("PASSWORD"), gbc); // 라벨 추가

        // 비밀번호 입력 필드
        gbc.gridx = 1; // 두 번째 열
        passwordField = new JPasswordField(); // 비밀번호 필드
        CommonStyle.underline(passwordField); // 밑줄 스타일
        add(passwordField, gbc);

        // 로그인 버튼
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        gbc.gridwidth = 2; // 두 열에 걸쳐 버튼 배치
        JButton loginButton = new JButton("LOGIN"); // 로그인 버튼 생성
        CommonStyle.stylePrimaryButton(loginButton);
        loginButton.addActionListener(e -> attemptLogin()); // 클릭 이벤트
        add(loginButton, gbc);

        // 메시지 출력 라벨
        gbc.gridy++; // 위치 변경
        messageLabel = new JLabel("", SwingConstants.CENTER); // 메시지용 라벨
        messageLabel.setForeground(CommonStyle.ERROR_COLOR); // 빨간 텍스트
        add(messageLabel, gbc);

        // 하단 링크 패널
        gbc.gridy++; // 위치 변경
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 링크 패널
        linkPanel.setBackground(Color.WHITE); // 배경 흰색

        JLabel signupLabel = new JLabel("회원가입"); // 회원가입
        signupLabel.setForeground(CommonStyle.LINK_COLOR); // 파란색
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 클릭 가능 커서

        // 회원가입 라벨을 클릭하면 현재 로그인 패널이 속한 최상위 프레임을 부모로 하여
        // JoinDialog(회원가입 창)를 모달로 띄움
        signupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // new JoinDialog((JFrame) SwingUtilities.getWindowAncestor(Login.this));
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Login.this);
                frame.setContentPane(new Signup()); // Signup 패널로 전환
                frame.revalidate();
                frame.repaint();
            }
        });

        JLabel forgotLabel = new JLabel("아이디&비밀번호 찾기"); // 아이디&비밀번호 찾기
        forgotLabel.setForeground(CommonStyle.LINK_COLOR); // 파란색
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 클릭 가능 커서

        // 회원가입 라벨을 클릭하면 현재 로그인 패널이 속한 최상위 프레임을 부모로 하여
        // ForgotDialog(아이디&비밀번호 찾기창)를 모달로 띄움
        forgotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // new ForgotDialog((JFrame) SwingUtilities.getWindowAncestor(Login.this));
                String[] options = { "아이디 찾기", "비밀번호 찾기" };
                int choice = JOptionPane.showOptionDialog(Login.this, "원하는 기능을 선택하세요.", "아이디/비밀번호 찾기",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                UserDAO dao = new UserDAO();

                // 아이디&비밀번호 찾기
                if (choice == 0) { // 아이디 찾기
                    String name = JOptionPane.showInputDialog(Login.this, "이름을 입력해주세요:");
                    if (name != null && !name.trim().isEmpty()) {
                        UserDTO user = new UserDTO();
                        user.setName(name.trim());
                        String foundId = dao.findIdByName(user);

                        if (foundId != null) {
                            JOptionPane.showMessageDialog(Login.this, "아이디: " + foundId);
                        } else {
                            JOptionPane.showMessageDialog(Login.this, "입력하신 이름으로 등록된 아이디가 없습니다.");
                        }
                    }
                } else if (choice == 1) { // 비밀번호 찾기
                    String id = JOptionPane.showInputDialog(Login.this, "아이디를 입력해주세요:");
                    String name = JOptionPane.showInputDialog(Login.this, "이름을 입력해주세요:");
                    if (id != null && name != null && !id.trim().isEmpty() && !name.trim().isEmpty()) {
                        UserDTO user = new UserDTO();
                        user.setId(id.trim());
                        user.setName(name.trim());
                        String foundPw = dao.findPwByIdAndName(user);
                        if (foundPw != null) {
                            JOptionPane.showMessageDialog(Login.this, "비밀번호: " + foundPw);
                        } else {
                            JOptionPane.showMessageDialog(Login.this, "일치하는 정보가 없습니다.");
                        }
                    }
                }
            }
        });

        linkPanel.add(signupLabel); // 링크 추가
        linkPanel.add(forgotLabel); // 링크 추가
        add(linkPanel, gbc); // 패널에 링크 추가
    }

    // 로그인 시도 메서드
    private void attemptLogin() {
        // 사용자로부터 입력받은 아이디와 비밀번호를 가져오고, 앞뒤 공백을 제거
        String id = usernameField.getText().trim();
        String pw = new String(passwordField.getPassword()).trim();

        // 아이디나 비밀번호가 비어있는지 체크
        // 비어 있다면, 오류 메시지를 출력하고 로그인 절차를 종료
        if (id.isEmpty() || pw.isEmpty()) {
            messageLabel.setText("아이디와 비밀번호를 모두 입력하세요."); // 오류 메시지 출력
            return;
        }

        UserDAO dao = new UserDAO(); // UserDAO 객체 생성 후, 로그인 시도
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setPw(pw);
        if (dao.login(user)) {
            messageLabel.setText(""); // 로그인 성공 시 메시지 초기화
            UserSearch.curUserID = id;
            JOptionPane.showMessageDialog(this, "로그인 성공!");
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.TODOLIST);
        } else {
            messageLabel.setText("아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }

    public class UserSearch {
        public static String curUserID;
    }

    // 테스트용 메인 메서드
    public static void main(String[] args) {
        new BaseFrame();
    }

}
