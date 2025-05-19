package pill.pillDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

import common.Session;
import dbConnection.DBManager;

/*
 * 작성자 : 김관호
 * 작성일 : 2025.05.16
 * 파일명 : PillAlramDAO.java
 * 설명 : PillAlram 에 대한 DAO
 */


public class PillAlramDAO {

    private int nextInt;

    /**
     * 알람을 등록하거나 갱신하는 메서드
     * @param selectedHour 선택한 시간 (시 단위)
     */
    public void registerAlarm(int selectedHour) {
        try (Connection con = DBManager.getConnection()) {
            // 알람 시간을 조회하는 SQL 쿼리
            String sql = "SELECT alramTime FROM PILL_ALRAM WHERE id = ?";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setString(1, Session.getUserId()); // 로그인한 사용자의 ID (예시)
            ResultSet rs = psmt.executeQuery();
            
            // 기존 알람이 존재하면 갱신, 그렇지 않으면 새로 등록
            if (rs.next()) {
                updateAlarm(selectedHour);
                return;
            }
            insertAlarm(selectedHour);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 기존 알람 시간을 갱신하는 메서드
     * @param selectedHour 선택한 시간 (시 단위)
     */
    public void updateAlarm(int selectedHour) {
        try (Connection con = DBManager.getConnection()) {
            // 알람 시간을 갱신하는 SQL 쿼리
            String sql = "UPDATE PILL_ALRAM SET AlramTime = ? WHERE id = ?";
            PreparedStatement psmt = con.prepareStatement(sql);
            
            // 선택한 시간을 LocalTime 형식으로 변환하여 설정
            LocalTime time = LocalTime.of(selectedHour, 0, 0);
            psmt.setTime(1, Time.valueOf(time));
            psmt.setString(2, Session.getUserId()); // 로그인한 사용자의 ID (예시)
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 새로운 알람 시간을 삽입하는 메서드
     * @param selectedHour 선택한 시간 (시 단위)
     */
    public void insertAlarm(int selectedHour) {
        try (Connection con = DBManager.getConnection()) {
            // 새로운 알람 정보를 삽입하는 SQL 쿼리
            String sql = "INSERT INTO PILL_ALRAM(alram_id, id, alramTime, date) VALUES(?, ?, ?, now())";
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setInt(1, nextInt); // 알람 ID 설정
            psmt.setString(2, Session.getUserId()); // 로그인한 사용자의 ID (예시)
            
            // 선택한 시간을 LocalTime 형식으로 변환하여 설정
            LocalTime time = LocalTime.of(selectedHour, 0, 0);
            psmt.setTime(3, Time.valueOf(time));
            psmt.executeQuery(); // 삽입 쿼리 실행
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 다음 알람 ID를 설정하는 메서드
     * 데이터베이스의 알람 ID 중 가장 큰 값에 1을 더하여 설정
     */
    public void settingNextInt() {
        try (Connection con = DBManager.getConnection()) {
            // 현재 알람 ID를 조회하는 SQL 쿼리
            String sql = "SELECT alram_id FROM PILL_ALRAM";
            PreparedStatement psmt = con.prepareStatement(sql);
            ResultSet rs = psmt.executeQuery();

            // 알람 ID 중 가장 큰 값을 계산하여 nextInt 설정
            while (rs.next()) {
                int x = rs.getInt(1);
                nextInt = Math.max(x, nextInt);
            }
            nextInt++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 다음 알람 ID를 반환하는 메서드
     * @return nextInt 다음 알람 ID
     */
    public int getNextInt() {
        return nextInt;
    }

}
