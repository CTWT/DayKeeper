package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dbConnection.DBManager; // DBManager 임포트

/*
 * 생성자 : 문원주
 * 생성일 : 25.05.15
 * 파일명 : UserDAO.java
 * 수정자 : 이주하
 * 수정일 : 25.05.15
 * 설명 : 로그인DAO 관리 파일
 */

public class UserDAO {

    // 로그인 메서드
    public boolean login(UserDTO user) {
        String sql = "SELECT * FROM user WHERE id = ? AND pw = ?"; // 로그인 쿼리

        // DB 연결
        try (Connection conn = DBManager.getConnection(); // DB 연결
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getId()); // 아이디 설정
            pstmt.setString(2, user.getPw()); // 비밀번호 설정

            // 쿼리 실행
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // 결과가 있으면 로그인 성공
            }

        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 로그 출력
            return false; // 로그인 실패
        }
    }

    // 회원가입
    public boolean insertUser(UserDTO user) {
        String sql = "INSERT INTO user (id, pw, name, date) VALUES (?, ?, ?, ?)"; // 회원가입 쿼리
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getId()); // 아이디 설정
            pstmt.setString(2, user.getPw()); // 비밀번호 설정
            pstmt.setString(3, user.getName()); // 이름 설정
            pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis())); // 현재 날짜 설정

            return pstmt.executeUpdate() > 0; // 1건 이상 삽입되었으면 true

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 아이디 중복확인
    public boolean isDuplicateId(UserDTO user) {
        String sql = "SELECT id FROM user WHERE id = ?"; // 아이디 중복확인 쿼리

        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getId()); // 아이디 설정
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 중복된 아이디가 있으면 true 반환

        } catch (Exception e) {
            e.printStackTrace();
            return true; // 예외 발생시 중복으로 처리
        }
    }

    // 이름으로 아이디 찾기
    public String findIdByName(UserDTO user) {
        String sql = "SELECT id FROM user WHERE name = ?"; // 아이디 찾기 쿼리

        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName()); // 이름 설정
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("id"); // 아이디 반환
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 아이디와 이름으로 비밀번호 찾기
    public String findPwByIdAndName(UserDTO user) {
        String sql = "SELECT pw FROM user WHERE id = ? AND name = ?";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getId()); // 아이디 설정
            pstmt.setString(2, user.getName()); // 이름 설정
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("pw"); // 비밀번호 반환
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}