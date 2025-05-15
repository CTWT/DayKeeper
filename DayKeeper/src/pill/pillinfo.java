package pill;

import java.util.HashMap;
import java.util.Map;

/*
 * 수업명 : Project DayKeeper
 * 이름 : 임해균
 * 작성자 : 임해균
 * 작성일 : 25.05.15
 * 파일명 : pillinfo.java
 * 설명 : 약 이름에 따른 설명과 복용 팁을 함께 관리하는 클래스
 */

public class pillinfo {

    // 설명과 복용 팁을 담는 내부 전용 클래스 (외부 클래스 생성 최소화)
    private class SupplementData {
        String description;
        String intakeTip;

        SupplementData(String description, String intakeTip) {
            this.description = description;
            this.intakeTip = intakeTip;
        }
    }

    // 약 이름(key)과 SupplementData(value)를 저장하는 맵
    private Map<String, SupplementData> supplementMap;

    // 생성자: 약 이름별 설명 및 복용 팁 등록
    public pillinfo() {
        supplementMap = new HashMap<>();

        supplementMap.put("비타민C", new SupplementData(
            "피로 회복, 항산화 작용, 감기 예방에 도움을 줍니다.",
            "식사 후 복용, 나눠서 복용 시 흡수율 증가"
        ));

        supplementMap.put("오메가3", new SupplementData(
            "혈중 중성지방 감소와 혈관 건강 개선에 효과가 있습니다.",
            "식후 복용, 냉장보관 시 비린 맛 감소"
        ));

        supplementMap.put("루테인", new SupplementData(
            "눈의 황반을 보호하고 시력 저하를 예방합니다.",
            "지방 포함된 아침 식사 후 복용"
        ));

        supplementMap.put("유산균", new SupplementData(
            "장내 환경을 개선하고 배변 활동에 도움을 줍니다.",
            "공복 또는 취침 전 복용"
        ));

        supplementMap.put("철분", new SupplementData(
            "빈혈 예방 및 피로 개선에 도움을 줍니다.",
            "공복 복용, 비타민C와 함께 복용 시 흡수율 증가"
        ));

        supplementMap.put("아연", new SupplementData(
            "면역력 증진과 상처 치유에 효과적입니다.",
            "식후 복용, 공복 시 메스꺼움 유발 가능"
        ));

        supplementMap.put("비타민D", new SupplementData(
            "뼈 건강과 칼슘 흡수에 중요한 역할을 합니다.",
            "지방 식사 후 복용, 햇빛 노출과 병행 시 효과 상승"
        ));

        supplementMap.put("칼슘", new SupplementData(
            "뼈와 치아 형성에 필요하며 골다공증 예방에 도움을 줍니다.",
            "비타민D와 함께 섭취, 자기 전 복용도 추천"
        ));

        supplementMap.put("홍삼", new SupplementData(
            "피로 개선과 면역력 증진에 도움이 됩니다.",
            "식전 또는 식후 공복 아닐 때 복용, 고혈압 주의"
        ));

        supplementMap.put("마그네슘", new SupplementData(
            "근육 기능 유지와 신경 안정에 도움을 줍니다.",
            "자기 전 복용 추천, 칼슘과 함께 섭취 가능"
        ));

        supplementMap.put("멀티비타민", new SupplementData(
            "여러 영양소를 보충하여 전반적인 건강 유지에 효과적입니다.",
            "아침 식사 후 복용, 공복 시 위장 불편 가능"
        ));

        supplementMap.put("비오틴", new SupplementData(
            "모발과 손톱 건강, 에너지 대사에 도움을 줍니다.",
            "공복 또는 식사 중 복용, 꾸준히 섭취해야 효과적"
        ));
    }

    /**
     * 약 이름을 입력받아 설명을 반환
     * @param name 영양제 이름
     * @return 설명 문자열 (없을 경우 안내 문구)
     */
    public String getDescription(String name) {
        SupplementData data = supplementMap.get(name);
        return (data != null) ? data.description : "설명이 등록되지 않은 영양제입니다.";
    }

    /**
     * 약 이름을 입력받아 복용 팁을 반환 ("복용 TIP : " 포함)
     * @param name 영양제 이름
     * @return 복용 팁 문자열 (없을 경우 안내 문구)
     */
    public String getIntakeTip(String name) {
        SupplementData data = supplementMap.get(name);
        return (data != null) ? "복용 TIP : " + data.intakeTip : "복용 팁이 등록되지 않은 영양제입니다.";
    }
}
