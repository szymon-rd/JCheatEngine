package pl.jacadev.jce.agent.tree.items;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import pl.jacadev.jce.agent.Agent;
import pl.jacadev.jce.agent.res.Controller;
import pl.jacadev.jce.agent.tree.Tree;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * @author JacaDev
 */
public class ObjectItem extends Item {
    private static final Image OBJECT_ICON = new Image(Controller.class.getResourceAsStream("icons/objectIcon.png"));

    private String name;
    private Object object;

    public ObjectItem(String name, Object object) {
        Objects.requireNonNull(object);
        setGraphic(new ImageView(OBJECT_ICON));
        this.name = name;
        this.object = object;
        openFields();
    }

    public ObjectItem(Object object) {
        this(null, object);
    }

    private void openFields() {
        System.out.println(object.getClass());
        System.out.println(object.getClass().getDeclaredFields());
        for (Field f : object.getClass().getDeclaredFields()) {
            if((f.getModifiers() & Modifier.STATIC) == 0) getChildren().add(new FieldItem(f, object));
        }
    }

    @Override
    void setupMenu(ContextMenu menu) {
        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(a -> rename());
        MenuItem remove = new MenuItem("Remove");
        remove.setOnAction(a ->{
            Action response = Dialogs.create()
                    .owner(Agent.primaryStage)
                    .title("Remove")
                    .masthead("Removing object " + toString())
                    .message("Are you ok with this?")
                    .actions(Dialog.Actions.YES, Dialog.Actions.NO)
                    .showConfirm();
            if(response == Dialog.Actions.YES){
                remove();
            }
        });
        menu.getItems().addAll(rename, remove);
    }

    private void rename() {
        Dialogs.create()
                .owner(Agent.primaryStage)
                .title("Rename")
                .message("Enter new name: ")
                .showTextInput(toString())
                .ifPresent(name -> {
                    this.setName(name);
                    refresh();
                });
    }

    private void remove(){
        Tree.remove(this);
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
