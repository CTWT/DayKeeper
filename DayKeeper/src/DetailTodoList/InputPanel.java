package DetailTodoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import common.CommonStyle;

/**
 * 생성자 : 유연우  
 * 생성일 : 25.05.18  
 * 파일명 : InputPanel.java  
 * 수정자 : 
 * 수정일 : 
 * 설명 : TODO 입력 화면. 제목 및 내용을 입력받아 DB 및 UI 목록에 저장함.
 */
public class InputPanel extends JPanel {

    private final DetailMainFrame frame;
    private JTextField titleField;
    private JTextArea contentArea;

    /**
     * 생성자 - UI 구성 및 버튼 이벤트 설정
     * 
     * @param frame 메인 프레임 참조
     */
    public InputPanel(DetailMainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== 상단 타이틀 =====
        add(CommonStyle.createTitleLabel(), BorderLayout.NORTH);

        // ===== 중앙 입력 폼 =====
        JPanel inputFormPanel = new JPanel(new GridBagLayout());
        inputFormPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        // 할일 제목 라벨
        JLabel titleLabel = CommonStyle.createLabel("할일 제목:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        inputFormPanel.add(titleLabel, gbc);

        // 할일 제목 입력 필드
        titleField = new JTextField(20);
        CommonStyle.underline(titleField);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        inputFormPanel.add(titleField, gbc);

        // 할일 내용 라벨
        JLabel contentLabel = CommonStyle.createLabel("할일 내용:");
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 5, 0);
        inputFormPanel.add(contentLabel, gbc);

        // 할일 내용 입력 영역 (TextArea + ScrollPane)
        contentArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        inputFormPanel.add(scrollPane, gbc);

        add(inputFormPanel, BorderLayout.CENTER);

        // ===== 하단 버튼 패널 (저장 / 닫기) =====
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        JButton saveBtn = new JButton("저장");
        CommonStyle.stylePrimaryButton(saveBtn);
        JButton closeBtn = new JButton("닫기");
        CommonStyle.stylePrimaryButton(closeBtn);

        btnPanel.add(saveBtn);
        btnPanel.add(closeBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // ===== 저장 버튼 클릭 이벤트 =====
        saveBtn.addActionListener(e -> handleSave());

        // ===== 닫기 버튼 클릭 이벤트 =====
        closeBtn.addActionListener(e -> {
            titleField.setText("");
            contentArea.setText("");
            frame.showPanel(DetailMainFrame.MAIN);
        });
    }

    /**
     * 저장 버튼 클릭 시 동작 처리
     * - 제목과 내용을 UI/DB에 저장
     */
    private void handleSave() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "할일 제목을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (frame.getTodoListModel().contains(title)) {
            JOptionPane.showMessageDialog(this, "이미 존재하는 제목입니다.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. UI에 데이터 저장
        frame.getTodoListModel().addElement(title);
        frame.getTodoContentMap().put(title, content);

        // 2. DB에 데이터 저장
        int todoId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        String userId = "daykeeper";
        char todoYn = 'N'; // 기본값: 미완료
        Date today = Date.valueOf(java.time.LocalDate.now());

        TodoDAO dao = new TodoDAO();
        dao.insertTodo(todoId, userId, title, content, todoYn, today);

        // 3. 입력창 초기화 및 화면 전환
        titleField.setText("");
        contentArea.setText("");
        frame.showPanel(DetailMainFrame.MAIN);
    }
}
