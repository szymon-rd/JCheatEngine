package pl.jacadev.jinjector.agent.tree.items;

import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseEvent;

/**
 * @author JacaDev
 */
public class JINTreeCell extends TreeCell<Item> {
    public JINTreeCell() {
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getItem().handleClick());
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(getTreeItem().getValue().toString());
            setGraphic(getTreeItem().getGraphic());
        }
    }
}
