package pl.jacadev.jce.agent.tree;

import pl.jacadev.jce.agent.tree.items.ClassItem;
import pl.jacadev.jce.agent.tree.items.PackageItem;

import java.util.Arrays;

/**
 * @author JacaDev
 */
public class ApplicationMap {
    public static String[] getPackages() {
        Class[] classes = TreeUtil.getLoadedClasses();
        String[] packages = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            Class c = classes[i];
            String className = c.getName();
            if ((className.contains("."))) packages[i] = className.substring(0, c.getName().lastIndexOf('.'));
        }
        return Arrays.stream(packages)
                .filter(a -> a != null)
                .toArray(String[]::new);
    }

    public static ClassItem[] getClasses(PackageItem p) {
        return Arrays.stream(TreeUtil.getLoadedClasses())
                .filter(a -> a.getName().substring(0, a.getName().lastIndexOf('.')).equals(p.getName()))
                .map(ClassItem::new)
                .toArray(ClassItem[]::new);
    }

    public static ClassItem[] getClassesInDefaultPack(){
        return Arrays.stream(TreeUtil.getLoadedClasses())
                .filter(a -> !a.getName().contains("."))
                .map(ClassItem::new)
                .toArray(ClassItem[]::new);
    }
}
