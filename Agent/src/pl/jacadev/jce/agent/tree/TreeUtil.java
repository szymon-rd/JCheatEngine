package pl.jacadev.jce.agent.tree;

import javafx.scene.control.TreeItem;
import pl.jacadev.jce.agent.Agent;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author JacaDev
 */
public class TreeUtil {
    public static Class<?>[] getLoadedClasses(){
        return Arrays.asList(Agent.INSTRUMENTATION.getInitiatedClasses(ClassLoader.getSystemClassLoader()))
                .stream().filter(a -> isVisible(a.getName()) && !a.isArray()).toArray(Class[]::new);
    }

    private static boolean isVisible(String path){
        return !path.startsWith("pl.jacadev.jce") && !path.startsWith("javafx") && !path.startsWith("sun");
    }

    public static boolean contains(TreeItem<?> item, Object obj){
        return item.getChildren().contains(obj);
    }

    @SuppressWarnings("unchecked")
    public static  <T> Optional<TreeItem<?>> findEqual(TreeItem<?> item, T t){
        return (Optional<TreeItem<?>>) item.getChildren()
                .stream().filter(t::equals).findFirst();
    }
}
