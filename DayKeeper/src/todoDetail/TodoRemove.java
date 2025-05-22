package todoDetail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import common.CommonStyle;
import dbConnection.TodoDAO;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.18
 * 파일명 : RemovePanel.java
 * 수정자 : 
 * 수정일 :
 * 설명 : todolist 할일 확인 및 삭제 가능 창
 */

public class TodoRemove extends JDialog {

    private JLabel requestTitle;
    private JLabel requestcontent;
    private TodoDetail parent;
    private int todoId;

    public TodoRemove(TodoDetail parent, int todoId, String selectedValue) {
        this.parent = parent;
        this.todoId = todoId;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setSize(500, 500);
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

        // 할일 제목
        JLabel titleLabel = CommonStyle.createLabel("할일 제목:");

        // ==== TodoRemove.java 수정 영역 시작 ====
        requestTitle = new JLabel(selectedValue);
        requestTitle.setBorder(new LineBorder(Color.BLACK));

        // 폰트 크기 및 라벨 크기 조절 (추가)
        requestTitle.setFont(new Font("Arial", Font.BOLD, 24));
        requestTitle.setPreferredSize(new Dimension(300, 30));
        requestTitle.setMinimumSize(new Dimension(300, 30));
        requestTitle.setMaximumSize(new Dimension(300, 30));


        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        contentPanel.add(requestTitle, gbc);

        // 할일 내용
        JLabel contentLabel = CommonStyle.createLabel("할일 내용:");
        requestcontent = new JLabel();
        requestcontent.setBorder(new LineBorder(Color.BLACK));
        requestcontent.setVerticalAlignment(JLabel.TOP); // 다중 줄 내용 위 정렬
        requestcontent.setPreferredSize(new Dimension(300, 100));
        requestcontent.setMinimumSize(new Dimension(300, 100));
        requestcontent.setMaximumSize(new Dimension(300, 100));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        contentPanel.add(contentLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        contentPanel.add(requestcontent, gbc);

        // 중앙에 배치하기 위해 감싸는 패널 사용
        JPanel centerWrapperPanel = new JPanel(new BorderLayout());
        centerWrapperPanel.setBackground(Color.WHITE);
        centerWrapperPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 5)); // 위쪽 여백만

        // contentPanel을 한 번만 add!
        centerWrapperPanel.add(contentPanel, BorderLayout.CENTER);

        updateData(selectedValue);

        // Frame에 추가
        add(centerWrapperPanel, BorderLayout.CENTER);

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
            String title = selectedValue;
            if (title != null && !title.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    TodoDAO.deleteTodo(todoId);
                    parent.deleteData(todoId);
                    parent.loadData();
                    dispose();
                }
            }
        });

        // 닫기 버튼 클릭 이벤트
        closeBtn.addActionListener(e -> {
            setVisible(false);
        });
    }

    // 패널이 화면에 보여질 때 호출해서 현재 선택된 할일 제목/내용 보여주도록
    public void updateData(String title) {
        String todoTitle = title;
        String content = parent.getTodoMap().get(title);
        requestTitle.setText(todoTitle);
        requestcontent.setText(content);

    }
}
