package todoList;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import config.BaseFrame;

public class TodoList extends JPanel {

    public TodoList() {
        setLayout(new GridBagLayout());

        JLabel jLabel = new JLabel("Dfdd");
        add(jLabel);

    }

    public static void main(String[] args) {
        BaseFrame b = new BaseFrame();
        TodoList t = new TodoList();
        b.setContentPane(t);
        b.setVisible(true);
    }
}
