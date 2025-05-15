package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dbConnection.DBManager; // DBManager 임포트

/*
 * 생성자 : 문원주
 * 생성일 : 25.05.17
 * 파일명 : UserDAO.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 로그인DAO 관리 파일
 */

public class UserDAO {

    // 로그인 메서드
    public boolean login(String id, String password) {
        String sql = "SELECT * FROM user WHERE id = ? AND pw = ?"; // 로그인 쿼리

        // DB 연결
        try (Connection conn = DBManager.getConnection(); // DB 연결
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id); // 아이디 설정
            pstmt.setString(2, password); // 비밀번호 설정

            // 쿼리 실행
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // 결과가 있으면 로그인 성공
            }

        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 로그 출력
            return false; // 로그인 실패
        }
    }
}