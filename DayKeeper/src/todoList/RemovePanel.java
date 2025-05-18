package todoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.18
 * 파일명 : DetailMainPanel.java
 * 수정자 : 
 * 수정일 :
 * 설명 : todolist 할일 확인 및 삭제 가능 창
 */

public class RemovePanel extends JPanel {

    private DetailMainFrame frame;

    private JLabel titleLabel;
    private JLabel contentLabel;

    public RemovePanel(DetailMainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 타이틀
        add(CommonStyle.createTitleLabel(), BorderLayout.NORTH);

        // 내용 표시 패널
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        titleLabel = CommonStyle.createLabel("");
        contentLabel = CommonStyle.createLabel("");

        contentPanel.add(titleLabel);
        contentPanel.add(contentLabel);

        add(contentPanel, BorderLayout.CENTER);

        // 삭제, 닫기 버튼 패널
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton removeBtn = new JButton("삭제");
        CommonStyle.stylePrimaryButton(removeBtn);
        JButton closeBtn = new JButton("닫기");
        CommonStyle.stylePrimaryButton(closeBtn);

        btnPanel.add(removeBtn);
        btnPanel.add(closeBtn);

        add(btnPanel, BorderLayout.NORTH);

        // 하단 공통 버튼 패널
        BottomPanelComponents bottomComp = CommonStyle.createBottomPanel();
        add(bottomComp.panel, BorderLayout.PAGE_END);

        // 삭제 버튼 클릭 이벤트
        removeBtn.addActionListener(e -> {
            String title = frame.getSelectedTitle();
            if (title != null && !title.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    frame.getTodoListModel().removeElement(title);
                    frame.getTodoContentMap().remove(title);
                    frame.showPanel(DetailMainFrame.MAIN);
                }
            }
        });

        // 닫기 버튼 클릭 이벤트
        closeBtn.addActionListener(e -> {
            frame.showPanel(DetailMainFrame.MAIN);
        });
    }

    // 패널이 화면에 보여질 때 호출해서 현재 선택된 할일 제목/내용 보여주도록
    public void updateData() {
        String title = frame.getSelectedTitle();
        String content = frame.getTodoContentMap().getOrDefault(title, "");
        titleLabel.setText("할일 제목: " + title);
        contentLabel.setText("할일 내용: " + content);
    }
}
