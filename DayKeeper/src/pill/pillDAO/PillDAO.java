package pill.pillDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbConnection.DBManager;
import pill.pillManager.PillDTO;
import pill.pillManager.PillManager;

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
        String sql = "SELECT pill_id, id, pillName, pillAmount, date FROM Pill WHERE id = ?";
        String curUserId = "12345"; // 현재 사용자 ID (예시)

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
        String sql = "SELECT pill_id, pillAmount FROM pill WHERE id = ?";
        String curUserId = "12345"; // 현재 사용자 ID (예시)

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

        String sql = "DELETE FROM pill WHERE pill_id = ?";

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
        String sql = "SELECT pill_id FROM Pill WHERE id = ?";
        String curUserId = "12345"; // 현재 사용자 ID (예시)

        try (Connection con = DBManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, curUserId);
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
            String sql = "UPDATE pill SET pillAmount = pillAmount - ? WHERE pill_id = ?";
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
            String sql = "SELECT pillAmount FROM pill WHERE pill_id = ?";
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
                String sql = "UPDATE pill SET pillAmount = ? WHERE pillName = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                int resultAmount = PillManager.getInst().getDataByName(pillName).getPillAmount() + amount;
                pstmt.setInt(1, resultAmount);
                pstmt.setString(2, pillName);
                pstmt.executeUpdate();
            } else {
                // 새로운 영양제라면 삽입
                String sql = "INSERT INTO pill(pill_id, id, pillName, pillAmount, date) VALUES (?, ?, ?, ?, now())";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, new PillDAO().getNextInt());
                pstmt.setString(2, "12345");
                pstmt.setString(3, pillName);
                pstmt.setInt(4, amount);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
