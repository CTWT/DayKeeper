package todoList;

/*
 * 생성자 : 유연우
 * 생성일 : 25.05.15
 * 파일명 : Todo.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 할 일 데이터 모델
 */

public class Todo {
    public String title;
    public String content;

    public Todo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return title;
    }
}
