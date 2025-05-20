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
 * 파일명 : PillDAO.java
 * 수정자 : 문원주
 * 수정일 : 25.05.19
 * 설명 : total 메서드 생성 및 
 */

public class PillDAO {

    public Boolean[] getWeeklyMedicationStatus(String userId, LocalDate baseDate) {
        Boolean[] status = new Boolean[7]; // 월 ~ 일

        String sql = "SELECT pillYn, date FROM PILLYN WHERE id = ?";
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String pillYn = rs.getString("pillYn");
                boolean taken = "Y".equalsIgnoreCase(pillYn);
            
                LocalDate recordDate = rs.getTimestamp("date").toLocalDateTime().toLocalDate();
                LocalDate monday = baseDate.with(DayOfWeek.MONDAY);
                LocalDate sunday = baseDate.with(DayOfWeek.SUNDAY);
            
                if (!recordDate.isBefore(monday) && !recordDate.isAfter(sunday)) {
                    int index = recordDate.getDayOfWeek().getValue() - 1; // 월~일 → 0~6
                    status[index] = taken;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public double getTotalPill(String userId, LocalDate baseDate) {
        double rate = 0.0;

        // 기준 날짜의 주간 일요일을 기준으로 누적 복약률 계산
        LocalDate sunday = baseDate.with(DayOfWeek.SUNDAY);

        // 주어진 일요일 날짜까지 복약률 계산 (복용 수 / 전체 수)
        String sql = "SELECT COUNT(*) AS total, " +
                "SUM(CASE WHEN pillYn = 'Y' THEN 1 ELSE 0 END) AS taken " +
                "FROM PILLYN WHERE id = ? AND date <= ?";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, sunday.toString()); // 기준 주간의 일요일까지

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    int taken = rs.getInt("taken");

                    if (total > 0) {
                        rate = (taken * 100.0) / total;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rate;
    }
}
