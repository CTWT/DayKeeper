package pill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import dbConnection.DBManager;

public class PillManager {
    private HashMap<String, String> pillInfo;
    private HashMap<Integer, PillDTO> pillsMap;

    private static PillManager instance;

    private PillManager() {
        pillInfo = new HashMap<>();
        pillsMap = new HashMap<>();

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

    public static PillManager getInst(){
        if(instance == null) {
            instance = new PillManager();
        }

        return instance;
    }
    public void LoadDBData() {
        try {
            pillsMap.clear();

            Connection con = DBManager.getConnection();
            String sql = "SELECT pill_id, id, pillName, pillDetail, pillAmount, date FROM Pill WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            String curUserId = "12345";//Login.UserSearch.curUserID;
            pstmt.setString(1, curUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                PillDTO pillDTO = new PillDTO();
                pillDTO.setPill_id(rs.getInt(1));
                pillDTO.setId(rs.getString(2));
                pillDTO.setPillName(rs.getString(3));
                pillDTO.setPillDetail(rs.getString(4));
                pillDTO.setPillAmount(rs.getInt(5));
                pillDTO.setDate(rs.getDate(6));           

                // 약 정보를 맵에 추가
                pillsMap.put(pillDTO.getPill_id(), pillDTO);
                System.out.println(pillDTO);
           }

            // 자원 해제
            rs.close();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearPillsData(){
        pillsMap.clear();
    }

    public HashMap<Integer, PillDTO> getPillsMap() {
        return pillsMap;
    }

    public HashMap<String, String> getPillInfo(){
        return pillInfo;
    }

    public String getDescription(String name){
        if(!pillInfo.containsKey(name)){
            System.out.println("없는 영양제에 대한 정보를 찾으려 했습니다.");
            return null;
        }

        return pillInfo.get(name);
    }

    public PillDTO getDatabyId(Integer id) {
        return pillsMap.get(id);
    }

    public PillDTO getDatabyName(String name){
        Iterator<Integer> iter = pillsMap.keySet().iterator();
        while(iter.hasNext()){
            PillDTO dto = pillsMap.get(iter.next());
            if(dto.getPillName() == name){
                return dto;
            }
        }

        return null;
    }
}