package pill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import dbConnection.DBManager;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.12
 * 파일명 : PillManager.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 알약의 정보 들과 DTO를 관리하는 클래스
 */

/**
 * Singleton 클래스: 영양제 정보를 관리하는 클래스
 * 데이터베이스로부터 영양제 정보를 로드하여 관리합니다.
 */
public class PillManager {
    private HashMap<String, String> pillInfo;  // 영양제 기본 정보
    private HashMap<Integer, PillDTO> pillsMap; // DTOMap
    private static PillManager instance;        // 싱글톤 인스턴스
    public static int nextInt = 0;

    // private 생성자: 싱글톤 패턴을 위한 private 접근 제어자 사용
    private PillManager() {
        pillInfo = new HashMap<>();
        pillsMap = new HashMap<>();
        initPillInfo();
    }

    // 싱글톤 인스턴스 반환 메서드
    public static PillManager getInst() {
        if (instance == null) {
            instance = new PillManager();
        }
        return instance;
    }

    /**
     * 영양제 기본 정보를 초기화합니다.
     * 이름과 효능을 매핑하여 HashMap에 저장합니다.
     */
    private void initPillInfo() {
        pillInfo.put("비타민C", "피로 회복, 항산화 작용, 감기 예방에 도움을 줍니다.");
        pillInfo.put("오메가3", "혈중 중성지방 감소와 혈관 건강 개선에 효과가 있습니다.");
        pillInfo.put("루테인", "눈의 황반을 보호하고 시력 저하를 예방합니다.");
        pillInfo.put("유산균", "장내 환경을 개선하고 배변 활동에 도움을 줍니다.");
        pillInfo.put("철분", "빈혈 예방 및 피로 개선에 도움을 줍니다.");
        pillInfo.put("아연", "면역력 증진과 상처 치유에 효과적입니다.");
        pillInfo.put("비타민D", "뼈 건강과 칼슘 흡수에 중요한 역할을 합니다.");
        pillInfo.put("칼슘", "뼈와 치아 형성에 필요하며 골다공증 예방에 도움을 줍니다.");
        pillInfo.put("홍삼", "피로 개선과 면역력 증진에 도움이 됩니다.");
        pillInfo.put("마그네슘", "근육 기능 유지와 신경 안정에 도움을 줍니다.");
        pillInfo.put("멀티비타민", "여러 영양소를 보충하여 전반적인 건강 유지에 효과적입니다.");
        pillInfo.put("비오틴", "모발과 손톱 건강, 에너지 대사에 도움을 줍니다.");
    }

    /**
     * 데이터베이스에서 영양제 정보를 로드합니다.
     */
    public void loadDBData() {
        String sql = "SELECT pill_id, id, pillName, pillDetail, pillAmount, date FROM Pill WHERE id = ?";
        String curUserId = "12345"; // 현재 사용자 ID (예시)

        try (Connection con = DBManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
             
            pstmt.setString(1, curUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                PillDTO pillDTO = new PillDTO();
                pillDTO.setPill_id(rs.getInt("pill_id"));
                pillDTO.setId(rs.getString("id"));
                pillDTO.setPillName(rs.getString("pillName"));
                pillDTO.setPillDetail(rs.getString("pillDetail"));
                pillDTO.setPillAmount(rs.getInt("pillAmount"));
                pillDTO.setDate(rs.getDate("date"));

                // 로드된 데이터를 Map에 저장
                pillsMap.put(pillDTO.getPill_id(), pillDTO);
                System.out.println("Loaded: " + pillDTO);
                nextInt = Math.max(nextInt,pillDTO.getPill_id());
            }
            nextInt++;
            rs.close();
        } catch (SQLException e) {
            System.err.println("데이터 로드 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 영양제 데이터 초기화 (맵 비우기)
     */
    public void clearPillsData() {
        pillsMap.clear();
    }

    /**
     * 영양제 상세 정보를 ID로 조회합니다.
     *
     * @param id 영양제 ID
     * @return PillDTO 객체 또는 null
     */
    public PillDTO getDataById(Integer id) {
        return pillsMap.get(id);
    }

    /**
     * 영양제 상세 정보를 이름으로 조회합니다.
     *
     * @param name 영양제 이름
     * @return PillDTO 객체 또는 null
     */
    public PillDTO getDataByName(String name) {
        for (Integer id : pillsMap.keySet()) {
            PillDTO dto = pillsMap.get(id);
            if (dto.getPillName().equals(name)) {
                return dto;
            }
        }
        return null;
    }

    /**
     * 영양제 설명을 이름으로 조회합니다.
     *
     * @param name 영양제 이름
     * @return 설명 문자열 또는 null
     */
    public String getDescription(String name) {
        if (!pillInfo.containsKey(name)) {
            System.out.println("영양제 정보가 없습니다: " + name);
            return null;
        }
        return pillInfo.get(name);
    }

    /**
     * 모든 영양제 정보를 반환합니다.
     *
     * @return 영양제 정보를 담고 있는 Map
     */
    public HashMap<Integer, PillDTO> getPillsMap() {
        return pillsMap;
    }

    /**
     * 기본 영양제 정보를 반환합니다.
     *
     * @return 영양제 설명 Map
     */
    public HashMap<String, String> getPillInfo() {
        return pillInfo;
    }
}