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
    public static Class<?>[] getLoadedClasses() {
        return Arrays.asList(Agent.INSTRUMENTATION.getInitiatedClasses(ClassLoader.getSystemClassLoader()))
                .stream().filter(a -> isVisible(a.getName()) && !a.isArray()).toArray(Class[]::new);
    }

    private static boolean isVisible(String path) {
        return !path.startsWith("pl.jacadev.jce") && !path.startsWith("javafx") && !path.startsWith("sun");
    }

    public static boolean contains(TreeItem<?> item, Object obj) {
        return item.getChildren().contains(obj);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<TreeItem<?>> findEqual(TreeItem<?> item, T t) {
        return (Optional<TreeItem<?>>) item.getChildren()
                .stream().filter(t::equals).findFirst();
    }

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
        }
        return values;
    }

}
