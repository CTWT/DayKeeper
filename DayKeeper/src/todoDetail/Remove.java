package todoDetail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import common.CommonStyle;
import common.CommonStyle.BottomPanelComponents;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.18
 * 파일명 : RemovePanel.java
 * 수정자 : 
 * 수정일 :
 * 설명 : todolist 할일 확인 및 삭제 가능 창
 */

public class Remove extends JDialog {

    private JLabel titleLabel;
    private JLabel contentLabel;

    public Remove() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setSize(500,500);
        setVisible(true);

        // 상단 타이틀
        add(CommonStyle.createTitleLabel(), BorderLayout.NORTH);

        // 내용 표시 패널
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        titleLabel = CommonStyle.createLabel("");

        gbc.gridx = 0;
        gbc.gridy = 0;

        contentLabel = CommonStyle.createLabel(" ");

        gbc.gridx =1;

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
        
        add(btnPanel, BorderLayout.SOUTH); // 한 번만 SOUTH에 추가

        // 삭제 버튼 클릭 이벤트
        removeBtn.addActionListener(e -> {
            String title = TodoDetailManager.getInst().getSelectedTitle();
            if (title != null && !title.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    TodoDetailManager.getInst().getTodoListModel().removeElement(title);
                    TodoDetailManager.getInst().getTodoContentMap().remove(title);
                    new TodoDetailDAO().deleteTodoByTitle("12345", title);
                    // Session.getUserId();
                    dispose();
                }
            }
        });

        // 닫기 버튼 클릭 이벤트
        closeBtn.addActionListener(e -> {
            dispose();
        });
    }

    // 패널이 화면에 보여질 때 호출해서 현재 선택된 할일 제목/내용 보여주도록
    public void updateData(String title) {
        String todoTitle = title;
        String content = TodoDetailManager.getInst().getTodoContentMap().getOrDefault(title, "");
        titleLabel.setText("할일 제목: " + todoTitle);
        contentLabel.setText("할일 내용: " + content);

        
    }
}
