package pl.jacadev.jce.agent.tree;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import pl.jacadev.jce.agent.Agent;
import pl.jacadev.jce.agent.utils.FieldValueSetter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author JacaDev
 */
public class TreeUtil {
    /**
     * @return initiated classes excluding those from JCheatEngine
     */
    public static Class<?>[] getLoadedClasses() {
        return Arrays.asList(Agent.INSTRUMENTATION.getInitiatedClasses(ClassLoader.getSystemClassLoader()))
                .stream().filter(a -> isVisible(a.getName()) && !a.isArray()).toArray(Class[]::new);
    }

    /**
     * @param path The path of class.
     * @return true if class should be visible
     */
    private static boolean isVisible(String path) {
        return !path.startsWith("pl.jacadev.jce") && !path.startsWith("javafx") && !path.startsWith("sun");
    }

    /**
     * @return true if treeItem contains object.
     */
    public static boolean contains(TreeItem<?> treeItem, Object obj) {
        return treeItem.getChildren().contains(obj);
    }

    /**
     * @return the first element of tree equal to element.
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<TreeItem<?>> findEqual(TreeItem<?> tree, T element) {
        return (Optional<TreeItem<?>>) tree.getChildren()
                .stream().filter(element::equals).findFirst();
    }

    /**
     * @param controls Controls of type TreeItem or TextField.
     * @param types Types for parsing values in controls.
     * @return Parsed to types values of controls in array.
     */
    public static Object[] getValues(Control[] controls, Class[] types) {
        Object[] values = new Object[controls.length];
        for (int i = 0; i < controls.length; i++) {
            Control control = controls[i];
            if (control instanceof TextField) {
                TextField field = (TextField) control;
                values[i] = FieldValueSetter.parse(field.getText(), types[i]);
            } else if (control instanceof ComboBox) {
                ComboBox comboBox = (ComboBox) control;
                values[i] = comboBox.getValue();
            }
            else throw new Error("Unsupported control type");
        }
        return values;
    }

}
