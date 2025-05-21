package todoList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import common.CommonStyle;
import common.Session;
import config.BaseFrame;
import config.ScreenType;
import todoDetail.TodoDetail;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.15
 * 파일명 : TodoList.java
 * 수정자 : 
 * 수정일 :
 * 설명 : TodoList화면
 */

public class TodoList extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private List<TodoDTO> todoList; // DTO 리스트로 변경

    private final String[] columnNames = { "할 일", "상태" };

    public TodoList() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 타이틀
        JLabel title = CommonStyle.createTitleLabel();
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        // 중앙 영역 패널
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(10, 100, 10, 100));

        // 서브 타이틀
        JLabel subTitle = new JLabel("todo-list");
        subTitle.setFont(new Font("Arial", Font.BOLD, 16));
        centerPanel.add(subTitle, BorderLayout.NORTH);

        // DB에서 목록 불러오기
        refreshTodoList();

        // 테이블 모델 생성
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 1) {
                    String yn = getValueAt(row, column).toString();
                    return "N".equals(yn);
                }
                return false;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                super.setValueAt(aValue, row, column);

                if (column == 1 && "Y".equals(aValue)) {
                    if (todoList != null && row < todoList.size()) {

                        // 리스트 재조회 및 테이블 갱신
                        refreshTodoList();
                        refreshTodoListModel();
                    }
                }
            }
        };

        refreshTodoListModel();

        table = new JTable(model);
        table.getTableHeader().setDefaultRenderer(new HeaderWithBorderRenderer());
        table.setGridColor(Color.LIGHT_GRAY); // 또는 원하는 색상
        table.setIntercellSpacing(new Dimension(8, 4));
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.setPreferredSize(new Dimension(600, 200));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        table.getColumn("상태").setCellRenderer(new ButtonRenderer());
        table.getColumn("상태").setCellEditor(new ButtonEditor(table, model));

        add(centerPanel, BorderLayout.CENTER);

        // 하단 공통 버튼 설정
        CommonStyle.BottomPanelComponents bottom = CommonStyle.createBottomPanel();

        bottom.todoDetail.setVisible(true);
        bottom.pillDetail.setVisible(true);
        bottom.statistics.setVisible(true);
        bottom.returnPage.setVisible(true);

        bottom.todoDetail.addActionListener(e -> {
            BaseFrame statFrame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            statFrame.showScreen(ScreenType.TODODETAIL);
        });
        bottom.pillDetail.addActionListener(e -> {
            BaseFrame pillFrame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            pillFrame.showScreen(ScreenType.PILL);
        });
        bottom.statistics.addActionListener(e -> {
            BaseFrame statFrame = (BaseFrame) SwingUtilities.getWindowAncestor(this);
            statFrame.showScreen(ScreenType.STATISTICS);
        });
        bottom.returnPage.setVisible(false);

        add(bottom.panel, BorderLayout.SOUTH);
    }

    // 현재 사용자의 해당하는 할 일 리스트 조회
    private void refreshTodoList() {
        try {
            todoList = TodoDAO.todoList(Session.getUserId());
            if (todoList == null) {
                todoList = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            todoList = new ArrayList<>();
        }
    }

    // 리스트 내용을 테이블 모델 반영
    private void refreshTodoListModel() {
        if (model == null)
            return;

        model.setRowCount(0); // 기존 데이터 제거

        for (TodoDTO item : todoList) {
            model.addRow(new Object[] { item.getTodoTitle(), item.getTodoYn() });
        }
        model.fireTableDataChanged(); // UI갱신
    }

    class HeaderWithBorderRenderer extends JLabel implements TableCellRenderer {
        public HeaderWithBorderRenderer() {
            setFont(new Font("맑은 고딕", Font.BOLD, 13));
            setOpaque(true);
            setBackground(new Color(240, 240, 240));
            setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1)); // 테두리 지정
            setHorizontalAlignment(CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    // 버튼 랜더링 N이면 버튼 Y면 텍스트 라벨
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            String val = value != null ? value.toString() : "";
            if ("N".equals(val)) {
                setText("미완료");
                setEnabled(true);
                return this;
            } else {
                return new JLabel("완료");
            }
        }
    }

    // 상태 컬럼 편집
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

        private JButton button;
        private String currentValue;
        private DefaultTableModel model;
        private JTable table;
        private int editingRow, editingColumn;

        // 생성자
        public ButtonEditor(JTable table, DefaultTableModel model) {
            this.table = table;
            this.model = model;

            button = new JButton();
            button.addActionListener(e -> {
                if ("N".equals(currentValue)) {
                    int result = JOptionPane.showConfirmDialog(
                            null,
                            "해당 할일을 완료 처리 하시겠습니까? \n완료하면 더 이상 수정할 수 없습니다.",
                            "할일 완료하기",
                            JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        model.setValueAt("Y", editingRow, editingColumn);

                        TodoDTO item = todoList.get(editingRow);
                        item.setTodoYn("Y");

                        TodoDAO.updateTodoYn(String.valueOf(item.getTodo_id()), Session.getUserId());

                        SwingUtilities.invokeLater(() -> {
                            refreshTodoList();
                            refreshTodoListModel();
                        });

                        refreshTodoList();
                        refreshTodoListModel();
                    }
                    fireEditingStopped();
                } else {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return currentValue;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            currentValue = value != null ? value.toString() : "N";
            editingRow = row;
            editingColumn = column;

            if ("N".equals(currentValue)) {
                button.setText("미완료");
                return button;
            } else {
                SwingUtilities.invokeLater(this::fireEditingStopped);
                return new JLabel("완료");
            }
        }
    }

    public static void main(String[] args) {
        BaseFrame b = new BaseFrame();
        TodoList t = new TodoList();
        b.setContentPane(t);
        b.setVisible(true);
    }
}