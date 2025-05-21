package dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 생성자 : 문원주
 * 생성일 : 25.05.16
 * 파일명 : StatisticsTodoDAO.java
 * 수정자 : 문원주
 * 수정일 : 25.05.19
 * 설명 : total 메서드 생성
 */

public class StatisticsTodoDAO {

    /**
     * 주어진 사용자 ID와 기준 날짜를 바탕으로
     * 해당 주의 월요일부터 일요일까지의 요일별 투두리스트 달성률을 계산한다.
     *
     * @param userId        사용자의 ID
     * @param referenceDate 기준 날짜 (기준 날짜가 속한 주를 계산 기준으로 사용)
     * @return 요일별 달성률(%)을 담은 Map 객체 (예: "월" -> 80.0)
     */

    public Map<String, Double> getWeeklyRate(String userId, LocalDate referenceDate) {
        // 결과를 담을 Map 객체 생성
        Map<String, Double> result = new HashMap<>();

        // 기준 날짜를 포함한 주의 월요일과 일요일 계산
        LocalDate monday = referenceDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate sunday = referenceDate.with(java.time.DayOfWeek.SUNDAY);

        // 날짜 포맷 설정 (yyyy-MM-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // SQL 쿼리: 요일별 전체 투두 개수와 달성된 개수를 카운트해서 비율 계산
        String sql = """
                SELECT
                    DAYOFWEEK(date) AS dayOfWeek,
                    COUNT(*) AS total,
                    SUM(CASE WHEN UPPER(todoYn) = 'Y' THEN 1 ELSE 0 END) AS done
                FROM TODO
                WHERE id = ?
                  AND DATE(date) BETWEEN ? AND ?
                GROUP BY dayOfWeek
                """;

        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 파라미터 바인딩
            pstmt.setString(1, userId); // 사용자 ID
            pstmt.setString(2, monday.format(formatter)); // 주 시작일 (월요일)
            pstmt.setString(3, sunday.format(formatter)); // 주 종료일 (일요일)

            // SQL 실행
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int dayOfWeek = rs.getInt("dayOfWeek"); // 요일 (1:일 ~ 7:토)
                    int total = rs.getInt("total"); // 전체 할 일 개수
                    int done = rs.getInt("done"); // 달성한 할 일 개수

                    // 달성률 계산 (예: 80.0%)
                    double rate = total == 0 ? 0.0 : (done * 100.0 / total);

                    // 요일명을 문자로 변환하여 Map에 저장
                    String day = convertDayOfWeek(dayOfWeek);
                    result.put(day, Math.round(rate * 10) / 10.0); // 소수점 1자리 반올림
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 예외 발생 시 로그 출력
        }

        return result; // 최종 결과 반환
    }

    /**
     * 일~토(1~7) 구조를 월~일(0~6)로 재정렬하기 위한 인덱스 조정
     * 
     * @param dayOfWeek 기존 요일 번호 (1=일, 2=월, ..., 7=토)
     * @return 재정렬된 요일 인덱스 (0=월, ..., 6=일)
     */
    private int getAdjustedDayIndex(int dayOfWeek) {
        if (dayOfWeek == 1)
            return 6;
        return dayOfWeek - 2;
    }

    // 숫자 요일(1~7)을 월요일 기준 한글 요일 문자열로 변환
    private String convertDayOfWeek(int dayOfWeek) {
        int adjustedIndex = getAdjustedDayIndex(dayOfWeek);
        return switch (adjustedIndex) {
            case 0 -> "월";
            case 1 -> "화";
            case 2 -> "수";
            case 3 -> "목";
            case 4 -> "금";
            case 5 -> "토";
            case 6 -> "일";
            default -> "";
        };
    }

    public double getTotalTodo(String userId, LocalDate baseDate) {
        double rate = 0.0;

        // baseDate 속한 주의 마지막 날(일요일)을 구함
        LocalDate sunday = baseDate.with(DayOfWeek.SUNDAY);

        String sql = "SELECT COUNT(*) AS total, " +
                "SUM(CASE WHEN todoYn = 'Y' THEN 1 ELSE 0 END) AS completed " +
                "FROM TODO WHERE id = ? AND date <= ?";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, sunday.toString()); // 선택한 주의 일요일까지 포함

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    int completed = rs.getInt("completed");

                    if (total > 0) {
                        rate = (completed * 100.0) / total;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rate;
    }
}
