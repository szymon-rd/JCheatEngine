package pl.jacadev.jce.agent.tree.items;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.jacadev.jce.agent.res.Controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author JacaDev
 */
public class ObjectItem extends Item {
    private static final Image OBJECT_ICON = new Image(Controller.class.getResourceAsStream("icons/objectIcon.png"));

    private String name;
    private Object object;

    public ObjectItem(String name, Object object) {
        setGraphic(new ImageView(OBJECT_ICON));
        this.name = name;
        this.object = object;
        openFields();
    }

    public ObjectItem(Object object) {
        this(null, object);
    }

    private void openFields() {
        for (Field f : object.getClass().getDeclaredFields()) {
            if((f.getModifiers() & Modifier.STATIC) == 0)getChildren().add(new FieldItem(f, object));
        }
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public void commitEdit(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType(){
        return object.getClass();
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return (name == null) ? object.toString() : name;
    }
}
