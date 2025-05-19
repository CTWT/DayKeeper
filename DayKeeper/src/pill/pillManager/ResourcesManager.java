package pill.pillManager;

import javax.swing.ImageIcon;

import java.awt.Image;
import java.util.HashMap;
import java.util.Iterator;

public class ResourcesManager {
    // 영양제 이름과 이미지를 매핑하여 저장하는 맵
    private HashMap<String, Image> resourcesMap;

    // 싱글톤 인스턴스
    private static ResourcesManager instance;

    /**
     * 기본 생성자: 맵 초기화
     */
    private ResourcesManager() {
        resourcesMap = new HashMap<>();
    }

    /**
     * 초기화 메서드: 리소스를 로드합니다.
     */
    private void init() {
        loadResources();
    }

    /**
     * 싱글톤 인스턴스 반환 메서드
     * - 인스턴스가 없으면 생성 후 초기화
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
     * 리소스를 로드하여 맵에 저장합니다.
     * - PillManager를 통해 영양제 이름 목록을 가져옵니다.
     * - 이름을 대문자로 변환하여 이미지 파일 경로를 구성합니다.
     * - 이미지를 로드하여 맵에 저장합니다.
     */
    private void loadResources() {
        // 영양제 이름 목록을 순회하는 반복자
        Iterator<String> iterator = PillManager.getInst().getPillInfo().keySet().iterator();

        while (iterator.hasNext()) {
            String name = iterator.next();
            String upperName = name.toUpperCase();
            // 이미지 파일 경로 구성
            String filePath = System.getProperty("user.dir") + "/DayKeeper/img/" + upperName + ".png";

            // 이미지를 로드하여 맵에 저장
            Image image = new ImageIcon(filePath).getImage();
            resourcesMap.put(upperName, image);
        }
    }

    /**
     * 이름으로 이미지를 가져오는 메서드
     * 
     * @param name 이미지 이름
     * @return 이미지 객체 (없으면 null)
     */
    public Image getImagebyName(String name) {
        return resourcesMap.get(name);
    }
}
