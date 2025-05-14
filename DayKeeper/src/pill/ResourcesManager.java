package pill;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ImageIcon;

public class ResourcesManager {
    private HashMap<String, Image> resourcesMap;

    private static ResourcesManager instance;

    private ResourcesManager() {
        resourcesMap = new HashMap<>();
    }

    private void init() {
        loadResources();
    }

    public static ResourcesManager getInst(){
        if(instance == null) {
            instance = new ResourcesManager();
            instance.init();
        }
        return instance;
    }

    private void loadResources() {
        HashMap<String, PillDTO> pillsMap = PillManager.getInst().getPillsMap();
        Iterator<String> iterator = pillsMap.keySet().iterator();

        while(iterator.hasNext()){
            String name = iterator.next();
            String upperName = name.toUpperCase();
            //resourcesMap.put(upperName, new ImageIcon(getClass().getResource("/Resources/" + upperName + ".png")).getImage());
        }

    }

    public Image getImage(String name) {
        String upperName = name.toString();
        Image image =  resourcesMap.get(upperName);
        return image;
    }

    public HashMap<String, Image> getResourceMap(){
        return resourcesMap;
    }
}
