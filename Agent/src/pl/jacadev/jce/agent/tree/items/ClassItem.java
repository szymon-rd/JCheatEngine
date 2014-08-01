package pl.jacadev.jce.agent.tree.items;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.jacadev.jce.agent.res.Controller;
import pl.jacadev.jce.agent.tree.cells.JCETreeCell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author JacaDev
 */
public class ClassItem extends Item {

    private static final Image CLASS_ICON = new Image(Controller.class.getResourceAsStream("icons/classIcon.png"));
    private static final Image ENUM_ICON = new Image(Controller.class.getResourceAsStream("icons/enumIcon.png"));
    private static final Image INTERFACE_ICON = new Image(Controller.class.getResourceAsStream("icons/interfaceIcon.png"));
    private static final Image ANNOTATION_ICON = new Image(Controller.class.getResourceAsStream("icons/annotationIcon.png"));
    private static final Image ABSTRACT_ICON = new Image(Controller.class.getResourceAsStream("icons/abstractIcon.png"));

    private static enum Type {
        CLASS(CLASS_ICON, true), ENUM(ENUM_ICON, false), INTERFACE(INTERFACE_ICON, false), ANNOTATION(ANNOTATION_ICON, false),
        ABSTRACT(ABSTRACT_ICON, false);

        private Image image;
        private boolean constructable;

        private Type(Image image, boolean constructable) {
            this.image = image;
            this.constructable = constructable;
        }

    }


    private ContextMenu menu;
    private final Class aClass;
    private boolean isLoaded = false;
    private final boolean constructable;

    public ClassItem(Class aClass) {
        this.aClass = aClass;
        Type type = getType(aClass);
        this.constructable = type.constructable;
        setGraphic(new ImageView(type.image));
        if(constructable){
            menu = new ContextMenu();
            MenuItem item = new MenuItem("New instance");
            item.setOnAction(event -> {
                //TODO create new instance
            });
            menu.getItems().add(item);
        }
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

    private static Type getType(Class<?> aClass) {
        if (aClass.isAnnotation()) return Type.ANNOTATION;
        if (aClass.isEnum()) return Type.ENUM;
        if (aClass.isInterface()) return Type.INTERFACE;
        if ((aClass.getModifiers() & Modifier.ABSTRACT) != 0) return Type.ABSTRACT;
        return Type.CLASS;
    }

    @Override
    public void updateCell(JCETreeCell cell) {
        if(constructable) cell.setContextMenu(menu);
    }
}
