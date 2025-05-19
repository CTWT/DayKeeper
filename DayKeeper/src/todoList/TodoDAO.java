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

    public static List<String[]> todoList(String id) {
        List<String[]> list = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT todoTitle, todoYn FROM TODO WHERE id = ? AND DATE(date) = CURDATE() ORDER BY todo_id")) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String todoTitle = rs.getString("todoTitle");
                String yn = rs.getString("todoYn");
                list.add(new String[] { todoTitle, yn });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String[]> todoDetailList(String id) {
        List<String[]> list = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECt todoTile, todoYN FROM TODO WHERE id = ? AND DATE(date) = CURDATE() ORDER BY todo_id")) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String todoTitle = rs.getString("todoTitle");
                String yn = rs.getString("todoYn");
                list.add(new String[] { todoTitle, yn });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insertDetailList(String id) {
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO TODO(todo_id, id, todoTitle , todoDetail, todoYn, date) VALUES(?, ?, ?, ?, ?, ?, now())")) {
            pstmt.setString(1, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
