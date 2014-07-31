package pl.jacadev.jinjector.agent.tree.items;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.jacadev.jinjector.agent.res.Controller;

import java.lang.reflect.Field;

/**
 * @author JacaDev
 */
public class FieldItem extends Item {
    private static final Image FIELD_ICON = new Image(Controller.class.getResourceAsStream("icons/fieldIcon.png"));

    private final Field field;

    public FieldItem(Field field) {
        setGraphic(new ImageView(FIELD_ICON));
        this.field = field;
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
        Controller.CONTROLLER.openFieldMenu(field);
    }

    @Override
    public void handleBranchExpansion() {

    }
}
