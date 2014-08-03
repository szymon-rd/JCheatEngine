package pl.jacadev.jce.agent.tree.items;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.jacadev.jce.agent.res.Controller;

import java.lang.reflect.Method;

/**
 * @author JacaDev
 */
public class MethodItem extends Item {
    private static final Image METHOD_ICON = new Image(Controller.class.getResourceAsStream("icons/methodIcon.png"));

    private Method method;

    public MethodItem(Method method){
        setGraphic(new ImageView(METHOD_ICON));
        this.method = method;
    }

    @Override
    public String toString() {
        return method.getName();
    }
}
