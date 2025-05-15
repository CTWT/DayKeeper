package todoList;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/*
 * 생성자 : 유연우
 * 생성일 : 25.05.15
 * 파일명 : TodoDetail.java
 * 수정자 : 
 * 수정일 :
 * 설명 : todo list 화면 구현
 */

public class TodoDetail extends JFrame {

    // 할 일 데이터 모델
    static class Todo {
        String title; // 할 일
        String content; // 할 일의 세부내용

        Todo(String title, String content) {
            this.title = title;
            this.content = content;
        }

        @Override
        public String toString() {
            // 아래 목록(JList)에서 뽑아낼때 제목만 나오도록 재정의
            return title;
        }
    }

    // 목록 생성(리스트)
    private DefaultListModel<Todo> todoListModel = new DefaultListModel<>(); // 저장소
    private JList<Todo> todoJList = new JList<>(todoListModel); //JList를 통해 화면에 뿌려줌

    // 화면 패널
    private JPanel mainPanel; // 메인
    private JPanel inputPanel; // 할일 입력
    private JPanel detailPanel; // 할일 확인

    // 입력화면 컴포넌트
    private JTextField titleInput;
    private JTextArea contentInput;

    // 상세화면 컴포넌트
    private JTextField detailTitleField;
    private JTextArea detailContentArea;

    public TodoDetail() {
        setTitle("daykeeper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null); // 창 가운데 설정
        setLayout(new CardLayout()); // 프레임의 레이아웃을 CardLayout으로 설정

        // 메인 화면 만들기
        mainPanel = createMainPanel();

        // 입력 화면 만들기
        inputPanel = createInputPanel();

        // 상세 화면 만들기
        detailPanel = createDetailPanel();

        add(mainPanel, "MAIN");
        add(inputPanel, "INPUT");
        add(detailPanel, "DETAIL");

        // main 화면 
        showPanel("MAIN");
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20)); // 20씩 여백

        JLabel titleLabel = new JLabel("Daykeeper"); // titlelabel 생성
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH); // 상단에 위치

        // 할 일 리스트 설정
        todoJList.setFont(new Font("Arial", Font.PLAIN, 16));
        todoJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 한번에 한개만 지정 가능
        JScrollPane scrollPane = new JScrollPane(todoJList); // 스크롤 영역 생성
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 검정 테두리
        panel.add(scrollPane, BorderLayout.CENTER);

        // 리스트 클릭 시 상세 화면 보여주기
        todoJList.addMouseListener(new MouseAdapter() { //이벤트 리스너 생성
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // 더블 클릭 시
                    // 선택한 내용이 없지 않다면 선택 일정을 보여줌
                    Todo selected = todoJList.getSelectedValue(); 
                    if (selected != null) {
                        showDetail(selected);
                    }
                }
            }
        });

        // 하단 버튼들
        JPanel buttonPanel = new JPanel(); // 버튼 패널 생성
        buttonPanel.setBackground(Color.white);
        JButton addButton = new JButton("할일 입력");
        JButton closeButton = new JButton("닫기");

        addButton.setBackground(new Color(100, 120, 255)); // 하늘색
        addButton.setForeground(Color.BLUE);
        addButton.setFocusPainted(false); // 테두리 노출 X

        closeButton.setBackground(new Color(100, 120, 255));
        closeButton.setForeground(Color.BLUE);
        closeButton.setFocusPainted(false);

        addButton.addActionListener(e -> showPanel("INPUT")); // 할일 버튼 클릭시 inputPanel로 이동
        closeButton.addActionListener(e -> System.exit(0)); // 닫기 버튼 클릭시 종료

        buttonPanel.add(addButton);
        buttonPanel.add(closeButton);

        // 버튼 패널 하단에 위치
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel; // 메인 페널 완성

    }// main Panel

    private JPanel createInputPanel() {

        JPanel panel = new JPanel(new GridBagLayout()); // 새 패널 생성
        panel.setBackground(Color.white);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20)); // 20씩 여백

        JLabel titleLabel = new JLabel("할일제목");
        JLabel contentLabel = new JLabel("할일내용");

        // 입력 필드 생성
        titleInput = new JTextField(20); // 할일 입력 텍스트필드
        contentInput = new JTextArea(5, 20); // 할일 내용 입력 5줄 가능
        contentInput.setLineWrap(true); // 자동 줄바꿈
        contentInput.setWrapStyleWord(true); // 단어 단위로 자동 줄바꿈
        JScrollPane contentScroll = new JScrollPane(contentInput);

        JButton saveButton = new JButton("저장");
        JButton closeButton = new JButton("닫기");

        saveButton.setBackground(new Color(100, 120, 255));
        saveButton.setForeground(Color.BLUE);
        saveButton.setFocusPainted(false); // 버튼 테두리 없애기

        closeButton.setBackground(new Color(100, 120, 255));
        closeButton.setForeground(Color.BLUE);
        closeButton.setFocusPainted(false);

        // 저장 버튼 누를경우
        saveButton.addActionListener(e -> { 
            // 공백 제거
            String title = titleInput.getText().trim();
            String content = contentInput.getText().trim();

            // 할일 이름 입력 X 경우
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "할일의 이름을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 정상 입력시
            todoListModel.addElement(new Todo(title, content)); // 리스트에 추가
            // 입력창 초기화
            titleInput.setText("");
            contentInput.setText("");
            showPanel("MAIN"); // main 창으로 돌아감
        });

        // 닫기 버튼 누를 경우
        closeButton.addActionListener(e -> {
            //초기화
            titleInput.setText("");
            contentInput.setText("");
            showPanel("MAIN");
        });

        // 
        GridBagConstraints gbc = new GridBagConstraints(); // gbc 객체 생성
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST; // 정렬을 왼쪽으로

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        gbc.gridx = 1;
        panel.add(titleInput, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(contentLabel, gbc);
        gbc.gridx = 1;
        panel.add(contentScroll, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(saveButton, gbc);
        gbc.gridx = 1;
        panel.add(closeButton, gbc);

        return panel;

    } // input panel

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(230, 230, 230));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 할일 입력하는 테이블 생성
        JLabel titleLabel = new JLabel("할일제목");
        detailTitleField = new JTextField(20);
        detailTitleField.setEditable(false);

        // Text 구역 구현
        detailContentArea = new JTextArea(7, 20);
        detailContentArea.setEditable(false);
        detailContentArea.setLineWrap(true);
        detailContentArea.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(detailContentArea);

        JButton deleteButton = new JButton("삭제");
        JButton inputButton = new JButton("오늘할일입력");
        JButton closeButton = new JButton("닫기");

        deleteButton.setBackground(new Color(100, 120, 255));
        deleteButton.setForeground(Color.BLUE);
        deleteButton.setFocusPainted(false);

        inputButton.setBackground(new Color(100, 120, 255));
        inputButton.setForeground(Color.BLUE);
        inputButton.setFocusPainted(false);

        closeButton.setBackground(new Color(100, 120, 255));
        closeButton.setForeground(Color.BLUE);
        closeButton.setFocusPainted(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        gbc.gridx = 1;
        panel.add(detailTitleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(contentScroll, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(deleteButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(inputButton, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(closeButton, gbc);

        deleteButton.addActionListener(e -> {
            Todo selected = todoJList.getSelectedValue();
            if (selected != null) {
                todoListModel.removeElement(selected);
                showPanel("MAIN");
            }
        });

        inputButton.addActionListener(e -> {
            titleInput.setText("");
            contentInput.setText("");
            showPanel("INPUT");
        });

        closeButton.addActionListener(e -> showPanel("MAIN"));

        return panel;
    }// detail panel

    private void showPanel(String name) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), name);
    }

    private void showDetail(Todo todo) {
        detailTitleField.setText(todo.title);
        detailContentArea.setText(todo.content);
        todoJList.setSelectedValue(todo, true);
        showPanel("DETAIL");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoDetail app = new TodoDetail();
            app.setVisible(true);
        });
    }
}
