package pl.jacadev.jinjector.agent.tree.items;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.jacadev.jinjector.agent.res.Controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author JacaDev
 */
public class ClassItem extends Item {
    private static final Image CLASS_ICON = new Image(Controller.class.getResourceAsStream("icons/classIcon.png"));

    private final Class aClass;
    private boolean isLoaded = false;

    public ClassItem(Class aClass) {
        this.aClass = aClass;
        setGraphic(new ImageView(CLASS_ICON));
    }

    public Class getAClass() {
        return aClass;
    }

    @Override
    public String toString() {
        String className = aClass.getName();
        return className.substring(className.lastIndexOf('.') + 1);
    }

    @Override
    public void handleClick() {
        if (!isLoaded) {
            openFields();
            openMethods();
            isLoaded = true;
        }
    }

    @Override
    public void handleBranchExpansion() {

    }

    private void openFields() {
        for (Field f : aClass.getDeclaredFields()) {
            getChildren().add(new FieldItem(f));
        }
    }

    private void openMethods() {
        for (Method m : aClass.getDeclaredMethods()) {
            getChildren().add(new MethodItem(m));
        }
    }
}
