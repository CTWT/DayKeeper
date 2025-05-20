package todoDetail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
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
public class Input extends JDialog {
    private JTextField titleField;
    private JTextField contentField;

    /**
     * InputPanel 생성자 - UI 초기화 및 이벤트 설정
     *
     * @param frame DetailMainFrame 참조
     */
    public Input() {

        setLayout(new BorderLayout());
        
        setSize(500, 500);
        
        setVisible(true);

        initUI();
        initEvent();
    }

    /**
     * UI 구성 초기화
     */
    private void initUI() {
        setBackground(Color.WHITE);

        JLabel title = CommonStyle.createTitleLabel();
        // 상단 타이틀
        add(title, BorderLayout.NORTH);

        // ===== 중앙 입력 폼 =====
        JPanel inputFormPanel = new JPanel(new GridBagLayout());
        inputFormPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // 할일 제목
        JLabel titleLabel = CommonStyle.createLabel("할일 제목:");
        
        JTextArea titleField = new JTextArea(1, 20);
        titleField.setBorder(new LineBorder(Color.BLACK));
        //CommonStyle.underline(titleField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputFormPanel.add(titleLabel, gbc);
        
        gbc.gridx = 1;
        //gbc.gridy = 0;
        inputFormPanel.add(titleField, gbc);
        
        // 할일 내용
        JLabel contentLabel = CommonStyle.createLabel("할일 내용:");
        JTextArea contentField = new JTextArea(5, 20);
        contentField.setBorder(new LineBorder(Color.BLACK));
        JScrollPane scrollPane = new JScrollPane(contentField);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;;
        inputFormPanel.add(contentLabel, gbc);

        gbc.gridx = 1;
        //gbc.gridy = 1;
        inputFormPanel.add(scrollPane, gbc);

        // 중앙에 배치하기 위해 감싸는 패널 사용
        JPanel centerWrapperPanel = new JPanel(new BorderLayout());
        centerWrapperPanel.setBackground(Color.WHITE);
        centerWrapperPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 5)); // 위쪽 여백만

        // inputFormPanel을 한 번만 add!
        centerWrapperPanel.add(inputFormPanel, BorderLayout.CENTER);

        // Frame에 추가
        add(centerWrapperPanel, BorderLayout.CENTER);


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
                        Input.this,
                        "할일 제목을 입력하세요.",
                        "경고",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (TodoDetailManager.getInst().getTodoListModel().contains(title)) {
                    JOptionPane.showMessageDialog(
                        Input.this,
                        "이미 존재하는 제목입니다.",
                        "경고",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // 데이터 추가
                TodoDetailManager.getInst().getTodoListModel().addElement(title);
                TodoDetailManager.getInst().setSelectedTitle(title);
                TodoDetailManager.getInst().getTodoContentMap().put(title, content);

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
