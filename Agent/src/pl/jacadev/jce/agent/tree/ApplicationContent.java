package pl.jacadev.jce.agent.tree;

import pl.jacadev.jce.agent.tree.nodes.ClassNode;
import pl.jacadev.jce.agent.tree.nodes.PackageNode;

import java.util.Arrays;

/**
 * @author JacaDev
 */
public class ApplicationContent {
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

    public static ClassNode[] getClasses(PackageNode p) {
        return Arrays.stream(TreeUtil.getLoadedClasses())
                .filter(a -> a.getName().substring(0, (a.getName().contains(".")) ? a.getName().lastIndexOf('.') : 0).equals(p.getName()))
                .map(ClassNode::new)
                .toArray(ClassNode[]::new);
    }

    public static ClassNode[] getClassesInDefaultPackage(){
        return Arrays.stream(TreeUtil.getLoadedClasses())
                .filter(a -> !a.getName().contains("."))
                .map(ClassNode::new)
                .toArray(ClassNode[]::new);
    }
}
