package pill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

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

    // 설명과 복용 팁을 담는 내부 전용 클래스 (외부 클래스 생성 최소화)
    private class SupplementData {
        String description;
        String intakeTip;

        SupplementData(String description, String intakeTip) {
            this.description = description;
            this.intakeTip = intakeTip;
        }
    }

    private HashMap<String, SupplementData> pillInfo;  // 영양제 기본 정보
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
        pillInfo.put("비타민C", new SupplementData(
            "피로 회복, 항산화 작용, 감기 예방에 도움을 줍니다.",
            "식사 후 복용, 나눠서 복용 시 흡수율 증가"
        ));

        pillInfo.put("오메가3", new SupplementData(
            "혈중 중성지방 감소와 혈관 건강 개선에 효과가 있습니다.",
            "식후 복용, 냉장보관 시 비린 맛 감소"
        ));

        pillInfo.put("루테인", new SupplementData(
            "눈의 황반을 보호하고 시력 저하를 예방합니다.",
            "지방 포함된 아침 식사 후 복용"
        ));

        pillInfo.put("유산균", new SupplementData(
            "장내 환경을 개선하고 배변 활동에 도움을 줍니다.",
            "공복 또는 취침 전 복용"
        ));

        pillInfo.put("철분", new SupplementData(
            "빈혈 예방 및 피로 개선에 도움을 줍니다.",
            "공복 복용, 비타민C와 함께 복용 시 흡수율 증가"
        ));

        pillInfo.put("아연", new SupplementData(
            "면역력 증진과 상처 치유에 효과적입니다.",
            "식후 복용, 공복 시 메스꺼움 유발 가능"
        ));

        pillInfo.put("비타민D", new SupplementData(
            "뼈 건강과 칼슘 흡수에 중요한 역할을 합니다.",
            "지방 식사 후 복용, 햇빛 노출과 병행 시 효과 상승"
        ));

        pillInfo.put("칼슘", new SupplementData(
            "뼈와 치아 형성에 필요하며 골다공증 예방에 도움을 줍니다.",
            "비타민D와 함께 섭취, 자기 전 복용도 추천"
        ));

        pillInfo.put("홍삼", new SupplementData(
            "피로 개선과 면역력 증진에 도움이 됩니다.",
            "식전 또는 식후 공복 아닐 때 복용, 고혈압 주의"
        ));

        pillInfo.put("마그네슘", new SupplementData(
            "근육 기능 유지와 신경 안정에 도움을 줍니다.",
            "자기 전 복용 추천, 칼슘과 함께 섭취 가능"
        ));

        pillInfo.put("멀티비타민", new SupplementData(
            "여러 영양소를 보충하여 전반적인 건강 유지에 효과적입니다.",
            "아침 식사 후 복용, 공복 시 위장 불편 가능"
        ));

        pillInfo.put("비오틴", new SupplementData(
            "모발과 손톱 건강, 에너지 대사에 도움을 줍니다.",
            "공복 또는 식사 중 복용, 꾸준히 섭취해야 효과적"
        ));
    }

    /**
     * 데이터베이스에서 영양제 정보를 로드합니다.
     */
    public void loadDBData() {
        String sql = "SELECT pill_id, id, pillName, pillAmount, date FROM Pill WHERE id = ?";
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
                pillDTO.setPillAmount(rs.getInt("pillAmount"));
                pillDTO.setDate(rs.getTimestamp("date").toLocalDateTime());

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

    //영양제 개수가 0이하인 데이터 삭제
    public void releaseData(){
        String sql = "SELECT pill_id, pillAmount FROM pill WHERE id = ?";
        String curUserId = "12345"; // 현재 사용자 ID (예시)

        try (Connection con = DBManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, curUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int pill_id = rs.getInt(1);
                int amount = rs.getInt(2);
                if(amount <= 0){
                    deleteDataById(pill_id);
                }
            }
        } catch (SQLException e) {
            System.err.println("데이터 릴리즈 오류: " + e.getMessage());
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
        return pillInfo.get(name).description;
    }

    /**
     * 영양제 설명을 이름으로 조회합니다.
     *
     * @param name 영양제 이름
     * @return 영양제 팁에 대한 문자열
     */
    public String getTip(String name) {
        if (!pillInfo.containsKey(name)) {
            System.out.println("영양제 정보가 없습니다: " + name);
            return null;
        }
        return pillInfo.get(name).intakeTip;
    }

    /**
     * 영양제 설명을 이름으로 조회합니다.
     *
     * @param id pill_id값
     * @return 해당 id의 영양제 삭제
     */
    public void deleteDataById(int id){
        if(pillsMap.containsKey(id)){
            pillsMap.remove(Integer.valueOf(id));
        }

        String sql = "delete from pill where pill_id =?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
             
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("데이터 로드 오류: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 영양제 설명을 이름으로 조회합니다.
     *
     * @param name 영양제 이름
     * @return 해당 이름의 영양제 삭제
     */
    public void deleteDataByName(String name) {
        Iterator<PillDTO> iter = pillsMap.values().iterator();
        
        while(iter.hasNext()){
            PillDTO dto = iter.next();
            if(dto.getPillName() == name){
                deleteDataById(dto.getPill_id());
                return;
            }
        }

        System.out.println("해당 이름의 데이터가 없습니다.");

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
    public HashMap<String, SupplementData> getPillInfo() {
        return pillInfo;
    }
}