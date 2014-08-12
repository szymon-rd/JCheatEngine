package pl.jacadev.jce.agent.tree.nodes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.jacadev.jce.agent.res.Controller;

import java.lang.reflect.Method;

/**
 * @author JacaDev
 */
public class MethodNode extends Node {
    private static final Image METHOD_ICON = new Image(Controller.class.getResourceAsStream("icons/methodIcon.png"));

    private Method method;

    public MethodNode(Method method){
        setGraphic(new ImageView(METHOD_ICON));
        this.method = method;
    }

    @Override
    void handleClick() {
        Controller.CONTROLLER.openMethodMenu(method, null);
    }

    @Override
    public String toString() {
        return method.getName();
    }
}
