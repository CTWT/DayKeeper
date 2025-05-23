package todoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import common.CommonStyle;
import dbConnection.TodoDTO;

public class TodoCardPanel extends JPanel {
    public TodoCardPanel(TodoDTO dto, Consumer<Integer> onComplete, boolean showButton,
            Consumer<TodoDTO> onDoubleClick) {
        setPreferredSize(new Dimension(180, 100));
        setLayout(new BorderLayout());
        setBackground(new Color(245, 250, 255));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel titleLabel = new JLabel(dto.getTodoTitle(), SwingConstants.CENTER);
        titleLabel.setFont(CommonStyle.TEXT_FONT);
        add(titleLabel, BorderLayout.CENTER);

        // 버튼 표시 여부에 따라 버튼 생성
        if (showButton) {
            JButton statusBtn = new JButton("미완료");
            if ("Y".equals(dto.getTodoYn())) {
                statusBtn.setText("완료");
                statusBtn.setEnabled(false);
            } else {
                statusBtn.addActionListener(e -> {
                    int result = JOptionPane.showConfirmDialog(this, "완료 처리하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        onComplete.accept(dto.getTodo_id());
                    }
                });
            }
            add(statusBtn, BorderLayout.SOUTH);
        }

        // 카드에 더블클릭 리스너 추가
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onDoubleClick.accept(dto);
                }
            }
        });
    }

    // 기존 생성자를 사용하던 곳을 위해 오버로드 생성자 추가 가능
    public TodoCardPanel(TodoDTO dto, Consumer<Integer> onComplete) {
        this(dto, onComplete, true, d -> {
        });
    }
}
