package pill;

import java.awt.Image;
import java.util.Iterator;

import javax.swing.ImageIcon;

public class testmain {
    public static void main(String[] args) {
        PillManager.getInst().LoadDBData();
        
        Iterator iter = PillManager.getInst().getPillsMap().values().iterator();
        while(iter.hasNext()){
            System.out.println(iter.next());
        }

        System.out.println(ResourcesManager.getInst().getResourceMap().size());
    }
}
