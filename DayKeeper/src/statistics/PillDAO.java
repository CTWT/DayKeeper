package statistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import dbConnection.DBManager;

/*
 * 생성자 : 이주하
 * 생성일 : 25.05.16
 * 파일명 : PhillDAO.java
 * 수정자 : 문원주
 * 수정일 : 25.05.19
 * 설명 : total 메서드 생성 및 
 */

public class PillDAO {

    public Boolean[] getWeeklyMedicationStatus(String userId) {
        Boolean[] status = new Boolean[7]; // 월 ~ 일 

        String sql = "SELECT pillYn, date FROM PILLYN WHERE id = ?";
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String pillYn = rs.getString("pillYn");
                boolean taken = "Y".equalsIgnoreCase(pillYn);
                String dateStr = rs.getString("date");

                LocalDate date = LocalDate.parse(dateStr);
                LocalDate now = LocalDate.now();
                DayOfWeek day = date.getDayOfWeek();
                int index = day.getValue() - 1; // 월=0 ~ 일=6

                // 이번 주 날짜만 반영
                LocalDate monday = now.with(DayOfWeek.MONDAY);
                LocalDate sunday = now.with(DayOfWeek.SUNDAY);

                if (!date.isBefore(monday) && !date.isAfter(sunday)) {
                    status[index] = taken;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public double getTotalPill(String userId) {
        double rate = 0.0;

        // 사용자별 복약 데이터 중 전체 수와 '복용함'인 항목 수를 집계하는 SQL 쿼리
        String sql = "SELECT COUNT(*) AS total, SUM(CASE WHEN pillYn = 'Y' THEN 1 ELSE 0 END) AS taken " +
                "FROM PILLYN WHERE id = ?";

        try (Connection conn = DBManager.getConnection(); // DB 연결 객체 생성
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            // SQL 실행 후 결과 집합 가져오기
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total"); // 전체 복약 기록 수
                    int taken = rs.getInt("taken"); // 복용한(Y) 복약 기록 수

                    // 전체 기록이 있을 경우에만 복약률 계산
                    if (total > 0) {
                        rate = (taken * 100.0) / total;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rate; // 계산된 복약률 반환
    }
}
