package todoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dbConnection.TodoDTO;

public class TodoCardPanel extends JPanel {
    public TodoCardPanel(TodoDTO dto, Consumer<Integer> onComplete) {
        setPreferredSize(new Dimension(180, 100));
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 240));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel titleLabel = new JLabel(dto.getTodoTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        add(titleLabel, BorderLayout.CENTER);

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
}