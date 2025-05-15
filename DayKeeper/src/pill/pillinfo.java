package pill;

import java.util.HashMap;
import java.util.Map;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 25.05.15
 * 파일명 : pillinfo.java
 * 설명 : 약 이름에 따른 설명을 관리하는 클래스 (HashMap + Getter 포함)
 */

public class pillinfo {

    private Map<String, String> supplementDescriptions;

    // 생성자: 약 설명 초기화
    public pillinfo() {
        supplementDescriptions = new HashMap<>();

        supplementDescriptions.put("비타민C", "피로 회복, 항산화 작용, 감기 예방에 도움을 줍니다.");
        supplementDescriptions.put("오메가3", "혈중 중성지방 감소와 혈관 건강 개선에 효과가 있습니다.");
        supplementDescriptions.put("루테인", "눈의 황반을 보호하고 시력 저하를 예방합니다.");
        supplementDescriptions.put("유산균", "장내 환경을 개선하고 배변 활동에 도움을 줍니다.");
        supplementDescriptions.put("철분", "빈혈 예방 및 피로 개선에 도움을 줍니다.");
        supplementDescriptions.put("아연", "면역력 증진과 상처 치유에 효과적입니다.");
        supplementDescriptions.put("비타민D", "뼈 건강과 칼슘 흡수에 중요한 역할을 합니다.");
        supplementDescriptions.put("칼슘", "뼈와 치아 형성에 필요하며 골다공증 예방에 도움을 줍니다.");
        supplementDescriptions.put("홍삼", "피로 개선과 면역력 증진에 도움이 됩니다.");
        supplementDescriptions.put("마그네슘", "근육 기능 유지와 신경 안정에 도움을 줍니다.");
        supplementDescriptions.put("멀티비타민", "여러 영양소를 보충하여 전반적인 건강 유지에 효과적입니다.");
        supplementDescriptions.put("비오틴", "모발과 손톱 건강, 에너지 대사에 도움을 줍니다.");
    }

    /**
     * 약 이름을 키로 받아 설명을 반환하는 getter
     * @param name 약 이름
     * @return 설명 텍스트 (없으면 기본 안내 메시지 반환)
     */
    public String getDescription(String name) {
        return supplementDescriptions.getOrDefault(name, "설명이 등록되지 않은 영양제입니다.");
    }
}
