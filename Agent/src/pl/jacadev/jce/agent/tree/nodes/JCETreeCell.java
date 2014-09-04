package pl.jacadev.jce.agent.tree.nodes;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseEvent;


/**
 * @author JacaDev
 */
public class JCETreeCell extends TreeCell<Node> {

    public JCETreeCell() {
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (!containsNull()) getItem().handleClick();
        });
    }

    @Override
    protected void updateItem(Node node, boolean empty) {
        super.updateItem(node, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(getItem().toString());
            setGraphic(getItem().getGraphic());
            ContextMenu menu = new ContextMenu();
            getItem().setupMenu(menu);
            setContextMenu(menu);
        }
    }

    public boolean containsNull(){
        return getItem() == null;
    }



}
