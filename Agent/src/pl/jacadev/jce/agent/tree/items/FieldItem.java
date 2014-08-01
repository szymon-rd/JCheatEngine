package pl.jacadev.jce.agent.tree.items;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.jacadev.jce.agent.res.Controller;

import java.lang.reflect.Field;

/**
 * @author JacaDev
 */
public class FieldItem extends Item {
    private static final Image FIELD_ICON = new Image(Controller.class.getResourceAsStream("icons/fieldIcon.png"));

    private final Field field;
    private final Object owner;

    public FieldItem(Field field, Object owner) {
        setGraphic(new ImageView(FIELD_ICON));
        this.field = field;
        this.owner = owner;
    }

    public FieldItem(Field field) {
        this(field, null);
    }

    public Field getField() {
        return field;
    }

    @Override
    public String toString() {
        return field.getName();
    }

    @Override
    public void handleClick() {
        try {
            Controller.CONTROLLER.openField(field, owner);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
