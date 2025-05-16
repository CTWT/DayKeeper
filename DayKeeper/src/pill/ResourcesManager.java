package pill;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.HashMap;
import java.util.Iterator;

public class ResourcesManager {
    private HashMap<String, Image> resourcesMap;

    private static ResourcesManager instance;

    private ResourcesManager() {
        resourcesMap = new HashMap<>();
    }

    private void init() {
        loadResources();
    }

    public static ResourcesManager getInst() {
        if (instance == null) {
            instance = new ResourcesManager();
            instance.init();
        }
        return instance;
    }

    private void loadResources() {
        Iterator<String> iterator = PillManager.getInst().getPillInfo().keySet().iterator();

        while (iterator.hasNext()) {
            String name = iterator.next();
            String upperName = name.toUpperCase();
            String filePath = System.getProperty("user.dir") + "/DayKeeper/img/" + upperName + ".png";

            Image image = new ImageIcon(filePath).getImage();
            resourcesMap.put(upperName, image);
        }
    }

    public Image  getImagebyName(String name) {
        return resourcesMap.get(name);
    }
}
