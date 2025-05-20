package DetailTodoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import common.CommonStyle;

/**
 * 생성자 : 유연우  
 * 생성일 : 25.05.18  
 * 파일명 : InputPanel.java  
 * 수정자 :
 * 수정일 :
 * 설명 : 할일을 입력할 수 있는 패널 (제목과 내용)  
 */
public class InputPanel extends JDialog {
    private JTextField titleField;
    private JTextField contentField;

    /**
     * InputPanel 생성자 - UI 초기화 및 이벤트 설정
     *
     * @param frame DetailMainFrame 참조
     */
    public InputPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setSize(500, 500);
        
        setVisible(true);

        initUI();
        initEvent();
    }

    /**
     * UI 구성 초기화
     */
    private void initUI() {
        // 상단 타이틀
        add(CommonStyle.createTitleLabel(), BorderLayout.NORTH);

        // ===== 중앙 입력 폼 =====
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

        // ===== 하단 버튼 영역 =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton saveBtn = new JButton("저장");
        JButton closeBtn = new JButton("닫기");
        CommonStyle.stylePrimaryButton(saveBtn);
        CommonStyle.stylePrimaryButton(closeBtn);

        btnPanel.add(saveBtn);
        btnPanel.add(closeBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // 버튼 이벤트 등록
        registerButtonEvents(saveBtn, closeBtn);
    }

    /**
     * 버튼 클릭 이벤트 등록
     *
     * @param saveBtn 저장 버튼
     * @param closeBtn 닫기 버튼
     */
    private void registerButtonEvents(JButton saveBtn, JButton closeBtn) {
        // 저장 버튼 클릭 시: 제목 유효성 검사 및 리스트에 추가
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText().trim();
                String content = contentField.getText().trim();

                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        InputPanel.this,
                        "할일 제목을 입력하세요.",
                        "경고",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (DetailTodoManager.getInst().getTodoListModel().contains(title)) {
                    JOptionPane.showMessageDialog(
                        InputPanel.this,
                        "이미 존재하는 제목입니다.",
                        "경고",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // 데이터 추가
                DetailTodoManager.getInst().getTodoListModel().addElement(title);
                DetailTodoManager.getInst().setSelectedTitle(title);
                DetailTodoManager.getInst().getTodoContentMap().put(title, content);

                // 입력 필드 초기화 후 메인 화면으로
                clearFields();
                dispose();
                Timestamp tstamp = new Timestamp(ABORT);
                Date ts = new Date(tstamp.getTime());
                new TodoDetailDAO().insertTodo("12345", title, content, 'a', ts);
            }
        });

        // 닫기 버튼 클릭 시: 필드 초기화 후 메인 화면으로 전환
        closeBtn.addActionListener(e -> {
            clearFields();
            dispose();
        });
    }

    /**
     * 입력 필드를 초기화하는 메서드
     */
    private void clearFields() {
        titleField.setText("");
        contentField.setText("");
    }

    /**
     * 이벤트 별도 초기화 메서드 (필요 시 확장 가능)
     */
    private void initEvent() {
        // 현재는 버튼 이벤트만 존재하며, UI 초기화 중 호출됨
    }
}
