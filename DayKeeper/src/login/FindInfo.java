package login;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import common.CommonStyle;
import config.BaseFrame;
import config.ScreenType;

/*
 * 생성자 : 이주하
 * 생성일 : 25.05.16
 * 파일명 : FindInfo.java
 * 수정자 : 
 * 수정일 : 
 * 설명 : 아이디/비밀번호 찾기 패널 설정
 */

public class FindInfo extends JDialog {

    // CardLayout을 사용하여 (메뉴, 아이디 찾기, 비밀번호 찾기) 전환
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final PwFind pwFindInstance = new PwFind();
    private final IdFind idFindInstance = new IdFind();

    /**
     * 생성자: FindInfo 기본 레이아웃 및 패널
     * 
     * @param parent 부모 프레임
     */
    public FindInfo(Frame parent) {
        super(parent, "DayKeeper", true);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false); // 크기 고정
        getContentPane().setBackground(Color.WHITE);

        // 각 패널을 카드 레이아웃에 추가
        cardPanel.add(buildMenuPanel(), "MENU");
        cardPanel.add(idFindInstance, "IDFIND");
        cardPanel.add(pwFindInstance, "PWFIND");

        add(cardPanel, BorderLayout.CENTER);
    }

    /**
     * 메인 메뉴 패널 생성 (타이틀, 전환 버튼, 닫기 버튼 포함)
     * 
     * @return 메뉴 패널
     */
    private JPanel buildMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);

        // 타이틀, 버튼, 닫기 버튼 패널 생성
        JPanel titlePanel = createTitlePanel("아이디 & 비밀번호 찾기");
        JPanel buttonPanel = createSwitchButtons();

        // 가운데 정렬
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 패널에 순서대로 추가
        menuPanel.add(titlePanel);
        menuPanel.add(Box.createVerticalStrut(30));
        menuPanel.add(buttonPanel);
        menuPanel.add(Box.createVerticalStrut(10));

        return menuPanel;
    }

    /**
     * 타이틀 패널 생성
     * 
     * @param title 타이틀 텍스트
     * @return 타이틀 패널
     */
    private JPanel createTitlePanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 22));
        label.setForeground(new Color(30, 100, 180));
        panel.add(label);

        return panel;
    }

    /**
     * 아이디 찾기/비밀번호 찾기 전환 버튼 패널 생성
     * 
     * @return 버튼 패널
     */
    private JPanel createSwitchButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        panel.setBackground(Color.WHITE);

        // "아이디 찾기" 버튼 생성 및 이벤트 연결
        JButton findIdButton = new JButton("아이디 찾기");
        CommonStyle.stylePrimaryButton(findIdButton);
        findIdButton.addActionListener(e -> {
            idFindInstance.reset();
            showPanel("IDFIND");
        });

        // "비밀번호 찾기" 버튼 생성 및 이벤트 연결
        JButton findPwButton = new JButton("비밀번호 찾기");
        CommonStyle.stylePrimaryButton(findPwButton);
        findPwButton.addActionListener(e -> {
            pwFindInstance.reset(); // 입력값 초기화
            showPanel("PWFIND"); // 비밀번호 찾기 패널로 전환
        });

        panel.add(findIdButton);
        panel.add(findPwButton);

        return panel;
    }

    private void showPanel(String name) {
        cardLayout.show(cardPanel, name);
    }

}