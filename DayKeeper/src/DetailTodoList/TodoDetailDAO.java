package DetailTodoList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbConnection.DBManager;

/**
 * 생성자 : 유연우  
 * 생성일 : 25.05.18  
 * 파일명 : TodoDAO.java  
 * 수정자 :  
 * 수정일 :  
 * 설명 : TODO 테이블에 대한 DB 작업을 담당하는 DAO 클래스
 */
public class TodoDetailDAO {
    public static int nextInt;
    /**
     * TODO 테이블에 새로운 할일을 삽입하는 메서드
     * 
     * @param todoId   할일 고유 번호 (직접 지정 / auto_increment 사용 시 생략 가능)
     * @param userId   사용자 ID
     * @param title    할일 제목
     * @param detail   할일 상세 내용
     * @param done     'Y' or 'N' (할일 완료 여부)
     * @param date     해당 할일의 날짜
     */
    public void insertTodo(String userId, String title, String detail, char done, Date date) {
        String sql = "INSERT INTO TODO (todo_id, id, todoTitle, todoDetail, todoYn, date) VALUES (?, ?, ?, ?, ?, now())";

        // DB 연결 및 쿼리 실행
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 파라미터 바인딩
            stmt.setInt(1, nextInt++);
            stmt.setString(2, "12345");
            stmt.setString(3, title);
            stmt.setString(4, detail);
            stmt.setBoolean(5, true);

            stmt.executeUpdate(); // 실행

        } catch (SQLException e) {
            System.err.println("[INSERT 실패] DB 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace(); // 상세 오류 로그
        }
    }

    /**
     * 특정 사용자(userId)의 할일 제목(title)을 기준으로 DB에서 삭제
     * 
     * @param userId 사용자 ID
     * @param title  삭제 대상 할일 제목
     * @return 삭제 성공 여부 (true: 성공, false: 실패)
     */
    public boolean deleteTodoByTitle(String userId, String title) {
        String sql = "DELETE FROM TODO WHERE id = ? AND todoTitle = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, title);

            int result = pstmt.executeUpdate(); // 삭제된 행 수
            return result > 0;

        } catch (SQLException e) {
            System.err.println("[DELETE 실패] DB 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void loadTodoByUser(String userId){
        String sql = "select todo_id, TodoTitle, TodoDetail from Todo";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet  rs = pstmt.executeQuery();

            while(rs.next())
            {
                int id = rs.getInt(1);
                String title =  rs.getString(2);
                String detail =  rs.getString(3);
                DetailTodoManager.getInst().getTodoContentMap().put(title, detail);
                DetailTodoManager.getInst().getTodoListModel().addElement(title);
                nextInt = Math.max(id,nextInt);
            }
            nextInt++;
            

        } catch (SQLException e) {
            System.err.println("[DELETE 실패] DB 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();

        }
    }

}
