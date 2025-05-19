package DetailTodoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;
//import DetailTodoList.TodoDAO;

/**
 * 생성자 : 유연우  
 * 생성일 : 25.05.18  
 * 파일명 : RemovePanel.java  
 * 수정자 :  
 * 수정일 :  
 * 설명 : 선택된 할일의 제목과 내용을 확인하고, 삭제 또는 닫기를 수행할 수 있는 화면
 */
public class RemovePanel extends JPanel {

    private final DetailMainFrame frame;
    private JLabel titleLabel;
    private JLabel contentLabel;

    /**
     * 생성자 - RemovePanel UI 구성 및 이벤트 연결
     * 
     * @param frame 메인 프레임 참조
     */
    public RemovePanel(DetailMainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== 상단 타이틀 =====
        add(CommonStyle.createTitleLabel(), BorderLayout.NORTH);

        // ===== 내용 표시 패널 (할일 제목/내용) =====
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        titleLabel = CommonStyle.createLabel("");   // 선택된 할일 제목 표시
        contentLabel = CommonStyle.createLabel(""); // 선택된 할일 내용 표시

        contentPanel.add(titleLabel);
        contentPanel.add(contentLabel);

        add(contentPanel, BorderLayout.CENTER);

        // ===== 버튼 패널 (삭제 / 닫기 버튼) =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton removeBtn = new JButton("삭제");
        JButton closeBtn = new JButton("닫기");
        CommonStyle.stylePrimaryButton(removeBtn);
        CommonStyle.stylePrimaryButton(closeBtn);

        btnPanel.add(removeBtn);
        btnPanel.add(closeBtn);

        // ===== 하단 공통 버튼 (홈/뒤로 등) =====
        BottomPanelComponents bottomComp = CommonStyle.createBottomPanel();

        // 두 패널을 감싸는 하단 패널
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.add(btnPanel, BorderLayout.NORTH);
        bottomWrapper.add(bottomComp.panel, BorderLayout.SOUTH);

        add(bottomWrapper, BorderLayout.SOUTH);

        // ===== 삭제 버튼 이벤트 =====
        removeBtn.addActionListener(e -> handleRemove());

        // ===== 닫기 버튼 이벤트 =====
        closeBtn.addActionListener(e -> frame.showPanel(DetailMainFrame.MAIN));
    }

    /**
     * 삭제 버튼 클릭 시 실행되는 로직 (UI 및 DB에서 할일 삭제)
     */
    private void handleRemove() {
        String title = frame.getSelectedTitle();

        if (title != null && !title.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "정말 삭제하시겠습니까?",
                "삭제 확인",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // ===== DB에서 삭제 =====
                TodoDAO dao = new TodoDAO();
                boolean success = dao.deleteTodoByTitle("daykeeper", title); // userId 하드코딩

                if (success) {
                    // UI에서도 삭제
                    frame.getTodoListModel().removeElement(title);
                    frame.getTodoContentMap().remove(title);
                    frame.showPanel(DetailMainFrame.MAIN);
                } else {
                    JOptionPane.showMessageDialog(this, "DB에서 삭제에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * 외부에서 호출되어 현재 선택된 할일 정보를 표시함
     */
    public void updateData() {
        String title = frame.getSelectedTitle();
        String content = frame.getTodoContentMap().getOrDefault(title, "");

        titleLabel.setText("할일 제목: " + title);
        contentLabel.setText("할일 내용: " + content);
    }
}
