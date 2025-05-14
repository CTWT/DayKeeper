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
 * 설명 : 영양제 인스턴스들을 관리해주는 Manager 클래스
 */

/**
 * Singleton 클래스: PillManager
 * 약 정보를 관리하는 클래스입니다.
 * 데이터베이스로부터 약 정보를 불러와 관리할 수 있습니다.
 */
public class PillManager {
    private HashMap<String, PillDTO> pillsMap;   // 약 정보를 저장하는 맵 (약 이름을 키로 사용)
    private HashMap<String, PillYnDTO> pillYnsMap; // 등록된 약의 정보를 저장하는 맵
    private static PillManager instance;         // Singleton 인스턴스
    public static int nextid = 0;

    // private 생성자: Singleton 패턴 구현
    private PillManager() {
        pillsMap = new HashMap<>();
        pillYnsMap = new HashMap<>();
    }

    /**
     * Singleton 인스턴스를 반환합니다.
     * 인스턴스가 없으면 새로 생성합니다.
     * 
     * @return PillManager 인스턴스
     */
    public static PillManager getInst() {
        if (instance == null) {
            instance = new PillManager();
        }
        return instance;
    }

    /**
     * 약 데이터를 추가합니다.
     * 
     * @param dto 추가할 PillDTO 객체
     */
    public void addYnData(PillYnDTO dto) {
        pillYnsMap.put(dto.getId(), dto);
    }

    /**
     * 약 이름으로 데이터를 조회합니다.
     * 
     * @param pillName 조회할 약 이름
     * @return 해당 약의 PillDTO 객체
     */
    public PillDTO getDatabyId(String id) {
        return pillsMap.get(id);
    }

    public PillDTO getPillDataByYn(PillYnDTO dto) {
        return pillsMap.get(dto.getId());
    }

    /**
     * 데이터베이스에서 약 정보를 불러옵니다.
     * 기존 데이터를 초기화한 후, 새로운 데이터를 저장합니다.
     */
    public void LoadDBData() {
        try {
            // 기존 데이터 초기화
            pillsMap.clear();

            // 데이터베이스 연결 및 SQL 실행
            Connection con = DBManager.getConnection();
            String sql = "SELECT id, pillName, pillDetail, pillAmount, date FROM Pill";
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // PillDTO 객체 생성 및 데이터 설정
                PillDTO pillDTO = new PillDTO();
                pillDTO.setId(rs.getString(1));               // ID 설정
                pillDTO.setPillName(rs.getString(2));          // 약 이름 설정
                pillDTO.setPillDetail(rs.getString(3));        // 약 상세 정보 설정
                pillDTO.setPillAmount(rs.getInt(4));           // 약 수량 설정
                pillDTO.setDate(rs.getDate(5));                // 날짜 설정

                // 약 정보를 맵에 추가
                pillsMap.put(pillDTO.getId(), pillDTO);
            }

            sql = "SELECT id, pillYn, date FROM PillYn";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                PillYnDTO pillYnDTO = new PillYnDTO();
                pillYnDTO.setId(rs.getString(1));
                pillYnDTO.setPillYn(rs.getString(2));
                pillYnDTO.setDate(rs.getDate(3));

                pillYnsMap.put(pillYnDTO.getId(), pillYnDTO);

                nextid = Math.max(nextid, Integer.parseInt(pillYnDTO.getId()));
            }
            nextid++;
            // 자원 해제
            rs.close();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 모든 약 정보를 포함하는 맵을 반환합니다.
     * 
     * @return 약 정보가 담긴 HashMap
     */
    public HashMap<String, PillDTO> getPillsMap() {
        return pillsMap;
    }

    public HashMap<String,PillYnDTO> getPillYnsMap() {
        return pillYnsMap;
    }
}