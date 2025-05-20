package todoDetail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;
import config.BaseFrame;
import config.ScreenType;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.20
 * 파일명 : TodoDetailPanel.java
 * 수정자 : 
 * 수정일 : 
 * 설명 : Detail 화면을 하나의 JPanel로 구성 - 
 */

public class TodoDetail extends JPanel {

    private DefaultListModel<String> todoListModel;
    private Map<String, String> todoContentMap;
    private String selectedTitle;

    public TodoDetail() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 초기화
        todoListModel = new DefaultListModel<>();
        todoContentMap = new HashMap<>();

        // 예시 데이터 로딩 (실제로는 DAO에서 조회)
        loadTodoByUser("12345");

        // 상단 타이틀
        JLabel title = CommonStyle.createTitleLabel();
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        // 리스트 구성
        JList<String> todoList = new JList<>(todoListModel);
        JScrollPane scrollPane = new JScrollPane(todoList);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.setBorder(new EmptyBorder(10, 100, 10, 100));
        add(scrollPane, BorderLayout.CENTER);

        // 입력 버튼
        JButton inputButton = new JButton("오늘할일입력");
        CommonStyle.stylePrimaryButton(inputButton);

        inputButton.addActionListener(e -> {
            JDialog dialog = new InputDialog();
            dialog.setVisible(true);
        });

        // 리스트 더블 클릭 시 삭제 다이얼로그
        todoList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = todoList.getSelectedValue();
                    if (selected != null) {
                        selectedTitle = selected;
                        JDialog dialog = new RemoveDialog(selected);
                        dialog.setVisible(true);
                    }
                }
            }
        });

        // 하단 버튼 구성
        BottomPanelComponents bottom = CommonStyle.createBottomPanel();
        bottom.todoDetail.setVisible(false); // 현재 화면
        bottom.pillDetail.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.PILL);
        });
        bottom.statistics.addActionListener(e -> {
            BaseFrame frame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            frame.showScreen(ScreenType.STATISTICS);
        });
        bottom.returnPage.setVisible(false);

        // 하단 전체 패널
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        JPanel inputWrapper = new JPanel();
        inputWrapper.add(inputButton);
        bottomWrapper.add(inputWrapper, BorderLayout.NORTH);
        bottomWrapper.add(bottom.panel, BorderLayout.SOUTH);
        add(bottomWrapper, BorderLayout.SOUTH);
    }

    // 샘플 데이터 로드
    private void loadTodoByUser(String userId) {
        todoListModel.addElement("운동하기");
        todoContentMap.put("운동하기", "30분 이상 걷기");

        todoListModel.addElement("약 복용");
        todoContentMap.put("약 복용", "혈압약, 당뇨약 복용");
    }

    // 입력 다이얼로그 클래스
    class InputDialog extends JDialog {
        public InputDialog() {
            setTitle("할일 입력");
            setSize(300, 200);
            setLocationRelativeTo(null);
            // 실제로는 입력 필드 구성 필요
            JLabel label = new JLabel("입력 다이얼로그입니다.");
            add(label);
        }
    }

    // 삭제 다이얼로그 클래스
    class RemoveDialog extends JDialog {
        public RemoveDialog(String title) {
            setTitle("할일 삭제");
            setSize(300, 200);
            setLocationRelativeTo(null);

            int result = JOptionPane.showConfirmDialog(
                this,
                "할일 '" + title + "' 을(를) 삭제하시겠습니까?",
                "삭제 확인",
                JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                todoListModel.removeElement(title);
                todoContentMap.remove(title);
            }
        }
    }

    // main 테스트용
    public static void main(String[] args) {
        BaseFrame f = new BaseFrame();
        f.setContentPane(new TodoDetail());
        f.setVisible(true); 
    }
}
