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
import javax.swing.SwingUtilities;

import config.BaseFrame;

/*
 * 생성자 : 이주하
 * 생성일 : 25.05.15
 * 파일명 : Signup.java
 * 수정자 : 이주하
 * 수정일 : 25.05.15
 * 설명 : 회원가입 기본 프레임 설정
 */

public class Signup extends JPanel {

    private JTextField newIdField; // 사용자 이름 입력 필드
    private JPasswordField newPwField; // 비밀번호 입력 필드
    private JTextField usernameField; // 사용자 이름 입력 필드
        private BaseFrame baseFrame;

    public Signup() {
        setLayout(new GridBagLayout()); // 레이아웃 설정
        setBackground(Color.WHITE); // 배경 흰색

        GridBagConstraints gbc = new GridBagConstraints(); // 위치 제약 설정
        gbc.insets = new Insets(8, 10, 8, 10); // 여백 설정
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 상단 제목: Sign Up
        JLabel headLabel = new JLabel("Sign Up", SwingConstants.CENTER); // 회원가입 제목
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
        add(common.CommonStyle.createLabel("ID"), gbc); // 라벨 추가

        // User Id 입력 필드
        gbc.gridx = 1; // 두 번째 열
        newIdField = new JTextField(); // 입력 필드
        common.CommonStyle.underline(newIdField);
        newIdField.setPreferredSize(new java.awt.Dimension(200, 20));
        add(newIdField, gbc);

        // User Id 중복확인 버튼
        gbc.gridx = 3; // 세 번째 열
        JButton checkButton = new JButton("중복확인");
        common.CommonStyle.stylePrimaryButton(checkButton);
        checkButton.addActionListener(e -> {
            String id = newIdField.getText();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "아이디를 입력해주세요.");
                return;
            }

            UserDAO dao = new UserDAO();
            UserDTO user = new UserDTO();
            user.setId(id);
            if ((dao.isDuplicateId(id))) {
                JOptionPane.showMessageDialog(this, "이미 사용 중인 아이디입니다.");
            } else {
                JOptionPane.showMessageDialog(this, "사용 가능한 아이디입니다!");
            }

        });
        add(checkButton, gbc);

        // Password 라벨
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        add(common.CommonStyle.createLabel("PASSWORD"), gbc); // 라벨 추가

        // Password 입력 필드
        gbc.gridx = 1; // 두 번째 열
        newPwField = new JPasswordField(); // 비밀번호 필드
        common.CommonStyle.underline(newPwField);
        newPwField.setPreferredSize(new java.awt.Dimension(200, 20));
        add(newPwField, gbc);

        // User name 입력 라벨
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        add(common.CommonStyle.createLabel("이름"), gbc); // 라벨 추가

        // User name 입력 필드
        gbc.gridx = 1; // 두 번째 열
        usernameField = new JTextField(); // 비밀번호 필드
        common.CommonStyle.underline(usernameField);
        usernameField.setPreferredSize(new java.awt.Dimension(200, 20));
        add(usernameField, gbc);

        // 뒤로가기 버튼
        gbc.gridy++; // 위치 변경
        gbc.gridx = 0; // 첫 번째 열
        JButton backButton = new JButton("뒤로가기"); // 뒤로가기 버튼 생성
        common.CommonStyle.stylePrimaryButton(backButton);
        backButton.addActionListener(e -> {
            baseFrame.setContentPane(new Login(baseFrame));
            baseFrame.revalidate();
            baseFrame.repaint();
        }); // 로그인 화면으로 돌아가기
        add(backButton, gbc);

        // 회원가입 버튼
        gbc.gridx = 1; // 두 번째 열
        JButton singupButton = new JButton("회원가입"); // 회원가입 버튼 생성
        common.CommonStyle.stylePrimaryButton(singupButton);
        singupButton.addActionListener(e -> {
            String id = newIdField.getText().trim();
            String pw = new String(newPwField.getPassword()).trim();
            String name = usernameField.getText().trim();

            if (!id.isEmpty() && !pw.isEmpty() && !name.isEmpty()) {
                UserDAO dao = new UserDAO();
                UserDTO user = new UserDTO(id, pw, name);
                if (dao.insertUser(user)) {
                    JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다!");
                    // 로그인 화면으로 이동
                    baseFrame.setContentPane(new Login(baseFrame));
                    baseFrame.revalidate();
                    baseFrame.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "회원가입에 실패했습니다.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "모든 필드를 입력해주세요.");
            }

        }); // 클릭 이벤트

        add(singupButton, gbc);

    }

    public Signup(BaseFrame baseFrame) {
        this();
        this.baseFrame = baseFrame;
    }


}
