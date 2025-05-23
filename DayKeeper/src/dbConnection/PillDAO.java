package dbConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import common.Session;
import pill.pillManager.PillManager;

/*
 * 작성자 : 김관호
 * 작성일 : 2025.05.16
 * 파일명 : PillDAO.java
 * 설명 : Pill 에 대한 DAO
 */

public class PillDAO {

    private int nextInt = 0;

    /**
     * 생성자: 객체 생성 시 nextInt 설정
     */
    public PillDAO() {
        settingNextInt();
    }

    /**
     * 데이터베이스에서 영양제 정보를 로드합니다.
     * - 사용자의 ID를 기반으로 영양제 데이터를 조회하여 Map에 저장합니다.
     */
    public void loadDBData() {
        String sql = "SELECT pill_id, id, pillName, pillAmount, date FROM PILL WHERE id = ?";
        String curUserId = Session.getUserId(); // 현재 사용자 ID (예시)

        try (Connection con = DBManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, curUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // PillDTO 객체에 데이터 저장
                PillDTO pillDTO = new PillDTO();
                pillDTO.setPill_id(rs.getInt("pill_id"));
                pillDTO.setId(rs.getString("id"));
                pillDTO.setPillName(rs.getString("pillName"));
                pillDTO.setPillAmount(rs.getInt("pillAmount"));
                pillDTO.setDate(rs.getTimestamp("date").toLocalDateTime());

                // 로드된 데이터를 Map에 저장
                PillManager.getInst().getPillsMap().put(pillDTO.getPill_id(), pillDTO);
                System.out.println("Loaded: " + pillDTO);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("데이터 로드 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 영양제 개수가 0 이하인 데이터를 삭제합니다.
     */
    public void releaseData() {
        String sql = "SELECT pill_id, pillAmount FROM PILL WHERE id = ?";
        String curUserId = Session.getUserId(); // 현재 사용자 ID (예시)

        try (Connection con = DBManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, curUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int pill_id = rs.getInt(1);
                int amount = rs.getInt(2);
                // 개수가 0 이하인 경우 데이터 삭제
                if (amount <= 0) {
                    deleteDataById(pill_id);
                }
            }
        } catch (SQLException e) {
            System.err.println("데이터 릴리즈 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 해당 ID의 영양제 데이터를 삭제합니다.
     * 
     * @param id 삭제할 영양제의 ID
     */
    public void deleteDataById(int id) {
        // Map에서 데이터 삭제
        if (PillManager.getInst().getPillsMap().containsKey(id)) {
            PillManager.getInst().getPillsMap().remove(Integer.valueOf(id));
        }

        String sql = "DELETE FROM PILL WHERE pill_id = ?";

        try (Connection con = DBManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("데이터 삭제 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 다음 삽입할 pill_id 값을 설정합니다.
     */
    private void settingNextInt() {
        String sql = "SELECT pill_id FROM PILL";

        try (Connection con = DBManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            // 가장 큰 pill_id 값을 가져와 다음 ID 설정
            while (rs.next()) {
                nextInt = Math.max(rs.getInt(1), nextInt);
            }
            nextInt++;
            rs.close();
        } catch (SQLException e) {
            System.err.println("다음 ID 설정 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 해당 ID의 영양제 개수를 감소시킵니다.
     * 
     * @param pillId 영양제 ID
     * @param amount 감소할 개수
     */
    public void consumePill(Integer pillId, Integer amount) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "UPDATE PILL SET pillAmount = pillAmount - ? WHERE pill_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, amount);
            pstmt.setInt(2, pillId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 영양제 개수를 조회합니다.
     * 
     * @param pillId 영양제 ID
     * @return 영양제 개수
     */
    public Integer getPillAmount(Integer pillId) {
        try (Connection con = DBManager.getConnection()) {
            String sql = "SELECT pillAmount FROM PILL WHERE pill_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, pillId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("pillAmount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 영양제 데이터를 삽입 또는 업데이트합니다.
     * 
     * @param pillName 영양제 이름
     * @param amount   영양제 개수
     */
    public void insertDrugToDB(String pillName, int amount) {
        try (Connection con = DBManager.getConnection()) {
            if (PillManager.getInst().getDataByName(pillName) != null) {
                // 이미 존재하는 영양제라면 수량 업데이트
                String sql = "UPDATE PILL SET pillAmount = ? WHERE pillName = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                int resultAmount = PillManager.getInst().getDataByName(pillName).getPillAmount() + amount;
                pstmt.setInt(1, resultAmount);
                pstmt.setString(2, pillName);
                pstmt.executeUpdate();
            } else {
                // 새로운 영양제라면 삽입
                String sql = "INSERT INTO PILL(pill_id, id, pillName, pillAmount, date) VALUES (?, ?, ?, ?, now())";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, new PillDAO().getNextInt());
                pstmt.setString(2, Session.getUserId());
                pstmt.setString(3, pillName);
                pstmt.setInt(4, amount);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean[] getWeeklyMedicationStatus(String userId, LocalDate baseDate) {
        Boolean[] status = new Boolean[7]; // 월 ~ 일

        String sql = "SELECT pillYn, date FROM PILLYN WHERE id = ? AND date BETWEEN ? AND ?";
        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 주간 범위 설정
            LocalDate monday = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate sunday = baseDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            // SQL 파라미터 세팅
            pstmt.setString(1, userId);
            pstmt.setTimestamp(2, Timestamp.valueOf(monday.atStartOfDay()));
            pstmt.setTimestamp(3, Timestamp.valueOf(sunday.atTime(LocalTime.MAX)));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String pillYn = rs.getString("pillYn");
                boolean taken = "Y".equalsIgnoreCase(pillYn);

                LocalDate recordDate = rs.getTimestamp("date").toLocalDateTime().toLocalDate();
                int index = recordDate.getDayOfWeek().getValue() - 1; // 월~일 → 0~6
                status[index] = taken;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public double getTotalPill(String userId, LocalDate baseDate) {
        double rate = 0.0;

        // 기준 주간의 일요일 설정
        LocalDate endDate = baseDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDate startDate = null;

        // 먼저 해당 사용자의 최초 복약 기록일 조회
        String getStartDateSql = "SELECT MIN(DATE(date)) AS startDate FROM PILLYN WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(getStartDateSql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                startDate = rs.getDate("startDate").toLocalDate();
            }

            if (startDate == null) {
                return 0.0; // 복약 기록이 하나도 없는 경우
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }

        // 날짜별로 복약 여부 확인
        int totalDays = 0;
        int takenDays = 0;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            totalDays++;

            String checkSql = "SELECT pillYn FROM PILLYN WHERE id = ? AND DATE(date) = ?";
            try (Connection conn = DBManager.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(checkSql)) {

                pstmt.setString(1, userId);
                pstmt.setDate(2, Date.valueOf(date));
                ResultSet rs = pstmt.executeQuery();

                boolean taken = false;
                while (rs.next()) {
                    if ("Y".equalsIgnoreCase(rs.getString("pillYn"))) {
                        taken = true;
                        break;
                    }
                }

                if (taken) {
                    takenDays++;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 복약률 계산
        if (totalDays > 0) {
            rate = (takenDays * 100.0) / totalDays;
        }

        return rate;
    }

    /**
     * 다음 삽입할 ID를 반환합니다.
     * 
     * @return 다음 ID 값
     */
    public int getNextInt() {
        return nextInt;
    }
}
