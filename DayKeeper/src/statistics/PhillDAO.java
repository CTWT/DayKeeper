package statistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import dbConnection.DBManager;

public class PhillDAO {


    public Boolean[] getWeeklyMedicationStatus(String userId) {
        Boolean[] status = new Boolean[7]; // 월 ~ 일
    
        String sql = "SELECT pillYn, date FROM PILLYN WHERE id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                boolean taken = rs.getBoolean("pillYn");
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
}


