package dbConnection;

public class TodoDTO {

    // 할일 ID
    private int todo_id;

    // 할일 타이틀
    private String todoTitle;

    // 할일 상세 내용
    private String todoDetail;

    // 할일 달성여부
    private String todoYn;

    public int getTodo_id() {
        return todo_id;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getTodoDetail() {
        return todoDetail;
    }

    public void setTodoDetail(String todoDetail) {
        this.todoDetail = todoDetail;
    }

    public void setTodo_id(int todo_id) {
        this.todo_id = todo_id;
    }

    public String getTodoYn() {
        return todoYn;
    }

    public void setTodoYn(String todoYn) {
        this.todoYn = todoYn;
    }

    @Override
    public String toString() {
        return todoTitle;
    }
}
