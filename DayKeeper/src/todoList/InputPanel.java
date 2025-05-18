package todoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import common.CommonStyle;
//import common.CommonStyle.BottomPanelComponents;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.18
 * 파일명 : InputPanel.java
 * 수정자 : 
 * 수정일 :
 * 설명 : todolist 할일 입력 창
 */

public class InputPanel extends JPanel {

    private DetailMainFrame frame;
    private JTextField titleField;
    private JTextField contentField;

    public InputPanel(DetailMainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 타이틀
        add(CommonStyle.createTitleLabel(), BorderLayout.NORTH);

        // 입력폼 (할일 제목, 내용)
        JPanel inputFormPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        inputFormPanel.setBackground(Color.WHITE);

        JLabel titleLabel = CommonStyle.createLabel("할일 제목:");
        titleField = new JTextField(20);
        CommonStyle.underline(titleField);

        JLabel contentLabel = CommonStyle.createLabel("할일 내용:");
        contentField = new JTextField(20);
        CommonStyle.underline(contentField);

        inputFormPanel.add(titleLabel);
        inputFormPanel.add(titleField);
        inputFormPanel.add(contentLabel);
        inputFormPanel.add(contentField);

        add(inputFormPanel, BorderLayout.CENTER);

        // 저장, 닫기 버튼 패널
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton saveBtn = new JButton("저장");
        CommonStyle.stylePrimaryButton(saveBtn);
        JButton closeBtn = new JButton("닫기");
        CommonStyle.stylePrimaryButton(closeBtn);

        btnPanel.add(saveBtn);
        btnPanel.add(closeBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // 저장 버튼 클릭 이벤트
        saveBtn.addActionListener((ActionEvent e) -> {
            String title = titleField.getText().trim();
            String content = contentField.getText().trim();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "할일 제목을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 중복 체크 (선택 사항)
            if (frame.getTodoListModel().contains(title)) {
                JOptionPane.showMessageDialog(this, "이미 존재하는 제목입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 데이터 추가
            frame.getTodoListModel().addElement(title);
            frame.getTodoContentMap().put(title, content);

            // 입력 필드 초기화
            titleField.setText("");
            contentField.setText("");

            // 메인 화면으로 전환
            frame.showPanel(DetailMainFrame.MAIN);
        });

        // 닫기 버튼 클릭 이벤트
        closeBtn.addActionListener(e -> {
            // 필드 초기화하고 메인 화면으로
            titleField.setText("");
            contentField.setText("");
            frame.showPanel(DetailMainFrame.MAIN);
        });
    }
}
