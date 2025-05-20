package todoList;

public class TodoDTO {

    private int todo_id;

    private String id;

    private String todoTitle;

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

    public void setTodo_id(int todo_id) {
        this.todo_id = todo_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTodoYn() {
        return todoYn;
    }

    public void setTodoYn(String todoYn) {
        this.todoYn = todoYn;
    }

}
