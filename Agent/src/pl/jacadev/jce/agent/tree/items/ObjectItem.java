package pl.jacadev.jce.agent.tree.items;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.dialog.Dialogs;
import pl.jacadev.jce.agent.Agent;
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
            if((f.getModifiers() & Modifier.STATIC) == 0) getChildren().add(new FieldItem(f, object));
        }
    }

    @Override
    void setupMenu(ContextMenu menu) {
        MenuItem item = new MenuItem("Rename");
        item.setOnAction(a -> rename());
        menu.getItems().add(item);
    }

    private void rename() {
        Dialogs.create()
                .owner(Agent.primaryStage)
                .title("Rename")
                .message("Please enter new name:")
                .showTextInput(toString())
                .ifPresent(name -> {
                    this.setName(name);
                    refresh();
                });
    }

    public void setName(String name) {
        this.name = name;
        setValue(this);
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
