package todoList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dbConnection.DBManager;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.15
 * 파일명 : TodoDAO.java
 * 수정자 : 
 * 수정일 :
 * 설명 : Todo sql
 */

public class TodoDAO {

    public static List<TodoDTO> todoList(String id) {
        List<TodoDTO> list = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT todo_id, todoTitle, todoYn FROM TODO WHERE id = ? AND DATE(date) = CURDATE() ORDER BY todo_id")) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TodoDTO dto = new TodoDTO();
                dto.setTodo_id(rs.getInt("todo_id"));
                dto.setTodoTitle(rs.getString("todoTitle"));
                dto.setTodoYn(rs.getString("todoYn"));

                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateTodoYn(String todo_id, String id) {
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE todo SET todoYn = 'Y' WHERE todo_id = ? AND id = ?")) {
            pstmt.setString(1, todo_id);
            pstmt.setString(2, id);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
