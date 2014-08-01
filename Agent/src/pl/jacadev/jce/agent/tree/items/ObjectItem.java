package pl.jacadev.jce.agent.tree.items;

import java.lang.reflect.Field;

/**
 * @author JacaDev
 */
public class ObjectItem extends Item {

    private String name;
    private Object object;

    public ObjectItem(String name, Object object) {
        this.name = name;
        this.object = object;

    }

    public ObjectItem(Object object) {
        this(null, object);
    }

    @Override
    void handleClick() {

    }
    private void openFields() {
        for (Field f : object.getClass().getDeclaredFields()) {
            getChildren().add(new FieldItem(f, object));
        }
    }

    @Override
    void handleBranchExpansion() {

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
