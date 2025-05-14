package pill;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.12
 * 파일명 : ResourcesManager.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 리소스를 관리하는 클래스
 */

/**
 * ResourcesManager 클래스는 이미지 리소스를 관리하는 싱글톤 클래스입니다.
 * 이미지 파일을 메모리에 로드하여 HashMap으로 관리합니다.
 */
public class ResourcesManager {
    // 이미지 리소스를 저장하는 HashMap (키: id 대문자, 값: Image 객체)
    private HashMap<String, Image> resourcesMap;

    // 싱글톤 인스턴스
    private static ResourcesManager instance;

    // private 생성자: 외부에서 직접 인스턴스 생성을 방지
    private ResourcesManager() {
        resourcesMap = new HashMap<>();
    }

    /**
     * 초기화 메서드: 리소스를 불러옵니다.
     */
    private void init() {
        loadResources();
    }

    /**
     * 싱글톤 인스턴스를 반환합니다.
     * 인스턴스가 없으면 초기화 후 반환합니다.
     * 
     * @return ResourcesManager 인스턴스
     */
    public static ResourcesManager getInst() {
        if (instance == null) {
            instance = new ResourcesManager();
            instance.init();
        }
        return instance;
    }

    /**
     * 이미지 리소스를 메모리에 로드하는 메서드
     * PillManager에서 모든 약 정보를 가져와 이미지 경로를 설정하고 로드합니다.
     */
    private void loadResources() {
        // PillManager로부터 약품 정보 맵 가져오기
        HashMap<String, PillDTO> pillsMap = PillManager.getInst().getPillsMap();
        Iterator<String> iterator = pillsMap.keySet().iterator();

        // 모든 약품 이름을 대문자로 변환하여 이미지로 매핑
        while (iterator.hasNext()) {
            String id = iterator.next();
            String name = pillsMap.get(id).getPillName();
            String upperName = name.toUpperCase();
            // 이미지 파일 경로 설정 (프로젝트 루트 기준)
            String filePath = System.getProperty("user.dir") + "/DayKeeper/img/" + upperName + ".png";

            // 이미지 로드 및 맵에 저장
            Image image = new ImageIcon(filePath).getImage();
            resourcesMap.put(id, image);

            // 이미지 로드 여부 확인 로그
            if (image == null) {
                System.err.println("Error: Unable to load image for " + upperName + " at " + filePath);
            } else {
                System.out.println("Image loaded: " + upperName + " from " + filePath);
            }
        }
    }

    /**
     * 이름으로 이미지를 가져옵니다.
     * 
     * @param name 이미지 이름
     * @return Image 객체 (없을 경우 null)
     */
    public Image getImagebyName(String name) {
        // 대문자로 변환하여 이미지 맵에서 검색
        Iterator<String> iter = resourcesMap.keySet().iterator();
        while(iter.hasNext()){
            String id = iter.next();
            String pillName = PillManager.getInst().getDatabyId(id).getPillName();
            if(name.equals(pillName))
            {
                return getImagebyId(id);
            }
        }

        System.out.println(name+"이미지가 존재하지 않습니다.");
        return null;
    }

    public Image getImagebyId(String id) {
        return resourcesMap.get(id);
    }

    public String getNamebyId(String id){
        return PillManager.getInst().getPillsMap().get(id).getPillName();
    }

    /**
     * 현재 메모리에 로드된 모든 이미지 리소스를 반환합니다.
     * 
     * @return 이미지 리소스 맵
     */
    public HashMap<String, Image> getResourceMap() {
        return resourcesMap;
    }
}
