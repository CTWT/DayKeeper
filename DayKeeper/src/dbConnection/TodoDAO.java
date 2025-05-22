package dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import common.Session;

/*
 * 생성자 : 신인철
 * 생성일 : 25.05.15
 * 파일명 : TodoDAO.java
 * 수정자 : 
 * 수정일 :
 * 설명 : Todo sql
 */

public class TodoDAO {

    /**
     * 
     * @param id 유저 id
     * @return
     */
    public static List<TodoDTO> todoList() {
        List<TodoDTO> list = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT todo_id, todoTitle, todoDetail, todoYn FROM TODO WHERE id = ? AND DATE(date) = CURDATE() ORDER BY todo_id")) {
            pstmt.setString(1, Session.getUserId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TodoDTO dto = new TodoDTO();
                dto.setTodo_id(rs.getInt("todo_id"));
                dto.setTodoTitle(rs.getString("todoTitle"));
                dto.setTodoDetail(rs.getString("todoDetail"));
                dto.setTodoYn(rs.getString("todoYn"));

                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 
     * @param todo_id 할일 id
     * @param id      유저 id
     */
    public static void updateTodoYn(int todo_id) {
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE TODO SET todoYn = 'Y' WHERE todo_id = ? AND id = ?")) {
            pstmt.setInt(1, todo_id);
            pstmt.setString(2, Session.getUserId());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param todo_id 할일 id
     * @param id      유저 id
     */
    public static void deleteTodo(int todo_id) {
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "DELETE FROM TODO WHERE todo_id = ? and id = ?")) {
            pstmt.setInt(1, todo_id);
            pstmt.setString(2, Session.getUserId());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param id         유저 id
     * @param todoTitle  할일 제목
     * @param todoDetail 할일 상세 내용
     */
    public static void insertTodo(String id, String todoTitle, String todoDetail) {
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO TODO(id, todoTitle, todoDetail, todoYn, date) VALUES(?, ?, ?, 'N', NOW())")) {
            pstmt.setString(1, id);
            pstmt.setString(2, todoTitle);
            pstmt.setString(3, todoDetail);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
